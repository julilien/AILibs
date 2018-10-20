package autofe.algorithm.hasco.evaluation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import autofe.algorithm.hasco.filter.meta.FilterPipeline;
import autofe.util.DataSet;
import autofe.util.EvaluationUtils;

public class COEDObjectEvaluator extends AbstractHASCOFEObjectEvaluator {

	private static final Logger logger = LoggerFactory.getLogger(COEDObjectEvaluator.class);

	@Override
	public Double evaluate(final FilterPipeline object) throws Exception {
		if (object == null) {
			return 20000d;
		}

		logger.debug("Applying and evaluating pipeline " + object.toString());
		DataSet dataSet = object.applyFilter(this.data, true);

		logger.debug("Applied pipeline. Starting benchmarking...");
		double loss = EvaluationUtils.calculateCOEDForBatch(dataSet.getInstances());

		logger.debug("COED object evaluation score: " + loss);
		// TODO
		// this.storeResult(object, loss, (System.currentTimeMillis() -
		// startTimestamp));
		return loss;
	}

}