package autofe.algorithm.hasco;

import autofe.algorithm.hasco.filter.meta.FilterPipeline;
import jaicore.basic.IObjectEvaluator;

public class AutoFEMLPreferredObjectEvaluator implements IObjectEvaluator<FilterPipeline, Double> {

	@Override
	public Double evaluate(FilterPipeline object) throws Exception {
		throw new UnsupportedOperationException("Not implemented yet.");
		// if (node.getParent() == null) {
		// return 0.0;
		// }
		//
		// List<String> remainingASTasks =
		// node.getPoint().getRemainingTasks().stream().map(x -> x.getProperty())
		// .filter(x -> x.startsWith("1_")).collect(Collectors.toList());
		// String appliedMethod = (node.getPoint().getAppliedMethodInstance() != null
		// ? node.getPoint().getAppliedMethodInstance().getMethod().getName()
		// : "");
		//
		// logger.trace("Remaining AS Tasks: " + remainingASTasks + " applied method: "
		// + appliedMethod);
		// boolean toDoHasAlgorithmSelection =
		// node.getPoint().getRemainingTasks().stream()
		// .anyMatch(x -> x.getProperty().startsWith("1_"));
		// ComponentInstance ci = this.getComponentInstanceFromNode(node);
		// AutoFEWekaPipeline pipe = this.getPipelineFromNode(node);
		// logger.trace("Todo has algorithm selection tasks {} Calculate node evaluation
		// for {}.", pipe);
		//
		// if (toDoHasAlgorithmSelection) {
		// if (pipe != null) {
		// if (pipe.getMLPipeline() != null && pipe.getMLPipeline() instanceof
		// MLPipeline) {
		// MLPipeline pipeline = (MLPipeline) pipe.getMLPipeline();
		// if (!pipeline.getPreprocessors().isEmpty()) {
		// for (SupervisedFilterSelector preprocessor : pipeline.getPreprocessors()) {
		// String evaluator = preprocessor.getEvaluator().getClass().getName();
		// String searcher = preprocessor.getSearcher().getClass().getName();
		//
		// boolean isSetEvaluator = evaluator.toLowerCase().matches(
		// ".*(relief|gainratio|principalcomponents|onerattributeeval|infogainattributeeval|correlationattributeeval|symmetricaluncertattributeeval).*");
		// boolean isRanker = searcher.toLowerCase().contains("ranker");
		// boolean isNonRankerEvaluator =
		// evaluator.toLowerCase().matches(".*(cfssubseteval).*");
		//
		// if (isSetEvaluator && !isRanker) {
		// logger.debug(
		// "We have a preprocessing step which is not a ranker but requires a set
		// evaluator, return {}",
		// 20000d);
		// return 20000d;
		// }
		// if (isNonRankerEvaluator && isRanker) {
		// logger.debug(
		// "We have a preprocessing step which is a ranker but requires a non-ranker
		// evaluator, return {}",
		// 20000d);
		// return 20000d;
		// }
		// }
		// }
		// }
		// if (pipe.getFilterPipeline() != null && pipe.getFilterPipeline().getFilters()
		// != null) {
		// if (pipe.getFilterPipeline().getFilters().size() > this.maxPipelineSize) {
		// logger.debug("We exceed the maximum number of image filters, so return {}",
		// MAX_EVAL_VALUE);
		// return MAX_EVAL_VALUE;
		// }
		//
		// double numFilters = pipe.getFilterPipeline().getFilters().size();
		// String classifierName;
		// boolean isMLPipeline = false;
		// if (pipe.getMLPipeline() instanceof MLPipeline) {
		// isMLPipeline = true;
		// classifierName = ((MLPipeline)
		// pipe.getMLPipeline()).getBaseClassifier().getClass().getName();
		// } else if (pipe.getMLPipeline() != null) {
		// classifierName = pipe.getMLPipeline().getClass().getName();
		// } else {
		// // TODO: Does it make sense to return 0 here?
		// // No ML pipeline
		// return 0.0;
		// }
		//
		// double indexOfClassifierName = this.classifiers.indexOf(classifierName);
		// if (indexOfClassifierName < 0) {
		// indexOfClassifierName = this.classifiers.size() + 1;
		// }
		//
		// double score = indexOfClassifierName;
		// if (isMLPipeline) {
		// score += this.classifiers.size() + 1;
		// }
		//
		// score += numFilters * (this.classifiers.size() + 1 * 2);
		// score /= 100000d;
		//
		// return score;
		// }
		//
		// if (ci != null &&
		// ci.getSatisfactionOfRequiredInterfaces().containsKey("mlPipeline")) {
		// ComponentInstance mlPipeline =
		// ci.getSatisfactionOfRequiredInterfaces().get("mlPipeline");
		// String classifierName = "";
		// double score = 0.0;
		// if (mlPipeline.getComponent().getProvidedInterfaces().contains("MLPipeline")
		// &&
		// mlPipeline.getSatisfactionOfRequiredInterfaces().containsKey("classifier")) {
		// classifierName =
		// mlPipeline.getSatisfactionOfRequiredInterfaces().get("classifier")
		// .getComponent().getName();
		// score += this.classifiers.size() + 1;
		// } else if
		// (!mlPipeline.getComponent().getProvidedInterfaces().contains("MLPipeline")) {
		// classifierName = mlPipeline.getComponent().getName();
		// }
		//
		// if (classifierName.equals("")) {
		// return 0.0;
		// }
		//
		// int index = this.classifiers.indexOf(classifierName);
		// if (index > 0) {
		// score += index;
		// } else {
		// score += this.classifiers.size() + 1;
		// }
		// return score / 10000;
		//
		// }
		// }
		// logger.trace("Still in algorithm selection phase => do BFS");
		// return 0.0;
		// } else {
		// logger.trace("Algorithm configuration stage => do random subsamples");
		// return null;
		// }
	}
}
