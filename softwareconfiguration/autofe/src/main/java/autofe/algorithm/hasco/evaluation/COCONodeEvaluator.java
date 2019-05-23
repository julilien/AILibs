package autofe.algorithm.hasco.evaluation;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import autofe.algorithm.hasco.filter.meta.FilterPipeline;
import autofe.util.DataSet;
import autofe.util.EvaluationUtils;
import hasco.exceptions.ComponentInstantiationFailedException;
import jaicore.ml.WekaUtil;
import jaicore.planning.hierarchical.algorithms.forwarddecomposition.graphgenerators.tfd.TFDNode;
import jaicore.search.algorithms.standard.bestfirst.exceptions.NodeEvaluationException;
import jaicore.search.model.travesaltree.Node;
import weka.core.Instances;

/**
 * Evaluator using the congenerous cosine distance (COCO) by Liu et. al., 2017 (cf. https://arxiv.org/pdf/1710.00870.pdf).
 *
 * @author Julian Lienen
 *
 */
public class COCONodeEvaluator extends AbstractHASCOFENodeEvaluator {

	private static final Logger logger = LoggerFactory.getLogger(COCONodeEvaluator.class);

	public COCONodeEvaluator(final int maxPipelineSize) {
		super(maxPipelineSize);
	}

	@Override
	public Double f(final Node<TFDNode, ?> node) throws NodeEvaluationException  {
		if (node.getParent() == null) {
			return null;
		}

		// If pipeline is too deep, assign worst value
		if (node.path().size() > maxPipelineSize) {
			return MAX_EVAL_VALUE;
		}

		FilterPipeline pipe;
		try {
			pipe = getPipelineFromNode(node);
		} catch (ComponentInstantiationFailedException e1) {
			throw new NodeEvaluationException(e1, "Could not evaluate pipeline.");
		}
		if (pipe != null && pipe.getFilters() != null) {
			try {
				logger.debug("Applying and evaluating pipeline " + pipe.toString());
				DataSet dataSet = pipe.applyFilter(data, true);

				// TODO
				// Get small batch
				List<Instances> split = WekaUtil.getStratifiedSplit(dataSet.getInstances(), 42, 0.01d);
				Instances insts = split.get(0);

				double loss = (-1) * EvaluationUtils.calculateCOCOForBatch(insts);

				logger.debug("COCO node evaluation score: " + loss);
				return loss;
			} catch (Exception e) {
				e.printStackTrace();
				logger.warn("Could not evaluate pipeline. Reason: " + e.getMessage());
				return null;
			}
		} else if (pipe == null) {
			logger.debug("Null pipe");
			return null;
		} else {
			logger.debug("Found a non-working pipeline.");
			return MAX_EVAL_VALUE;
		}
	}
}
