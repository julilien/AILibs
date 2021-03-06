package autofe.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import autofe.algorithm.hasco.filter.generic.AddConstantFilter;
import autofe.algorithm.hasco.filter.generic.AddRandomFilter;
import autofe.algorithm.hasco.filter.generic.IdentityFilter;
import autofe.algorithm.hasco.filter.generic.WEKAFilter;
import autofe.algorithm.hasco.filter.image.LocalBinaryPatternFilter;
import autofe.algorithm.hasco.filter.meta.IFilter;
import autofe.algorithm.hasco.filter.meta.UnionFilter;
import hasco.model.Component;
import hasco.model.ComponentInstance;
import weka.filters.unsupervised.attribute.PrincipalComponents;

public final class FilterUtils {
	private FilterUtils() {
		// Utility class
	}

	public static IFilter getFilterForName(final String name, final Map<String, String> parameters,
			final long[] inputShape) {
		// Image filters
		if (name.startsWith("autofe.algorithm.hasco.filter.image.CatalanoWrapperFilter")) {
			String[] filterNameSplit = name.split("_");
			return ImageUtils.getCatalanoFilterByName(filterNameSplit[1]);
		}

		if (name.startsWith("CatalanoExtractor")) {
			String[] extractorNameSplit = name.split("_");
			return ImageUtils.getCatalanoFilterByName(extractorNameSplit[1]);
		}

		if (name.startsWith("autofe.algorithm.hasco.filter.image")) {
			switch (name) {
			case "autofe.algorithm.hasco.filter.image.LocalBinaryPatternFilter":
				return new LocalBinaryPatternFilter();
			case "autofe.algorithm.hasco.filter.image.PretrainedNN":
				String net = parameters.get("net");
				net = net == null ? "" : net;
				int layer = -1;
				if (parameters.get("layer") != null)
					layer = Integer.parseInt(parameters.get("layer"));
				return ImageUtils.getPretrainedNNFilterByName(net, layer, inputShape);
			}
		}

		switch (name) {
		case "autofe.algorithm.hasco.filter.generic.AddConstantFilter":
			return new AddConstantFilter();
		case "autofe.algorithm.hasco.filter.generic.IdentityFilter":
			System.out.println("Returning default filter due to name occurence '" + name + "'.");
			return new IdentityFilter();
		case "autofe.algorithm.hasco.filter.generic.AddRandomFilter":
			return new AddRandomFilter();
		case "autofe.MakeUnion":
			return new UnionFilter();
		case "PCA":
			return new WEKAFilter(new PrincipalComponents());
		default:
			System.out.println("Returning default filter due to name occurence '" + name + "'.");
			return getDefaultFilter();
		}
	}

	public static IFilter getDefaultFilter() {
		return new IdentityFilter();
	}

	public static List<Component> getDefaultComponents() {
		final List<Component> components = new ArrayList<>();

		Component c = new Component("autofe.algorithm.hasco.filter.generic.AddConstantFilter");
		c.addProvidedInterface("filter");
		components.add(c);

		Component c1 = new Component("autofe.algorithm.hasco.filter.generic.IdentityFilter");
		c1.addProvidedInterface("filter");
		components.add(c1);

		Component c2 = new Component("FilterPipeline");
		c2.addRequiredInterface("filter", "filter");
		components.add(c2);

		Component c3 = new Component("autofe.algorithm.hasco.filter.generic.AddRandomFilter");
		c3.addProvidedInterface("filter");
		components.add(c3);

		return components;
	}

	/**
	 * Pretty print utility function used to print deep component instance
	 * hierarchies also printing the filters of wrapper filters.
	 *
	 * @param ci
	 *            Starting component instance of the hierarchy
	 * @param offset
	 *            Tabular offset
	 * @return Returns a textual representation of the component structure
	 */
	public static String getPrettyPrint(final ComponentInstance ci, final int offset) {
		StringBuilder sb = new StringBuilder();

		switch (ci.getComponent().getName()) {
		case "autofe.algorithm.hasco.filter.image.CatalanoWrapperFilter":
		case "CatalanoExtractor":
			sb.append(ci.getParameterValues().get("catFilter") + "\n");
			break;
		case "PretrainedNN":
			sb.append(ci.getParameterValues().get("net") + ": " + ci.getParameterValues().get("layer") + "\n");
			break;
		default:
			sb.append(ci.getComponent().getName() + "\n");
		}
		for (String requiredInterface : ci.getComponent().getRequiredInterfaces().keySet()) {
			for (int i = 0; i < offset + 1; i++) {
				sb.append("\t");
			}
			sb.append(requiredInterface);
			sb.append(": ");
			if (ci.getSatisfactionOfRequiredInterfaces().containsKey(requiredInterface)) {
				sb.append(getPrettyPrint(ci.getSatisfactionOfRequiredInterfaces().get(requiredInterface), offset + 1));
			} else {
				sb.append("null\n");
			}
		}
		return sb.toString();
	}
}
