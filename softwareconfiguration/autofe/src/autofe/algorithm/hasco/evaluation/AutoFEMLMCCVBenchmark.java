package autofe.algorithm.hasco.evaluation;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import autofe.algorithm.hasco.AutoFEWekaPipeline;
import autofe.util.DataSet;
import jaicore.basic.IObjectEvaluator;
import jaicore.basic.SQLAdapter;
import jaicore.ml.evaluation.MonteCarloCrossValidationEvaluator;
import jaicore.ml.evaluation.MulticlassEvaluator;
import weka.classifiers.Classifier;
import weka.core.Instances;

public class AutoFEMLMCCVBenchmark implements IObjectEvaluator<AutoFEWekaPipeline, Double> {
	private static final Logger logger = LoggerFactory.getLogger(AutoFEMLMCCVBenchmark.class);

	private final DataSet data;
	private final Random rand;
	private final int repeats;
	private final double trainingPortion;

	private SQLAdapter adapter;
	private int experimentID;
	private String evalTable;

	public SQLAdapter getAdapter() {
		return this.adapter;
	}

	public void setAdapter(final SQLAdapter adapter) {
		this.adapter = adapter;
	}

	public int getExperimentID() {
		return this.experimentID;
	}

	public void setExperimentID(final int experimentID) {
		this.experimentID = experimentID;
	}

	public String getEvalTable() {
		return this.evalTable;
	}

	public void setEvalTable(final String evalTable) {
		this.evalTable = evalTable;
	}

	public AutoFEMLMCCVBenchmark(final DataSet data, final long seed, final int repeats, final double trainingPortion) {
		this.data = data;
		this.rand = new Random(seed);
		this.repeats = repeats;
		this.trainingPortion = trainingPortion;
	}

	@Override
	public Double evaluate(final AutoFEWekaPipeline object) throws Exception {
		long startTimestamp = System.currentTimeMillis();
		Instances wekaInstances = object.transformData(this.data);
		IObjectEvaluator<Classifier, Double> internalEvaluator = new MonteCarloCrossValidationEvaluator(
				new MulticlassEvaluator(this.rand), this.repeats, wekaInstances, (float) this.trainingPortion);
		Double evalScore = internalEvaluator.evaluate(object);
		logger.info("Eval score of AUtoFEWekaPipeline " + object.toString() + " was " + evalScore);

		if (!evalScore.toString().equals("NaN")) {
			this.storeResult(object, evalScore, (System.currentTimeMillis() - startTimestamp));
		}
		return evalScore;
	}

	protected void storeResult(final AutoFEWekaPipeline pipe, final Double score, final long timeToCompute)
			throws SQLException {
		Map<String, Object> data = new HashMap<>();
		data.put("run_id", this.experimentID);
		data.put("errorRate", score);
		data.put("preprocessor", pipe.getFilterPipeline().toString());
		data.put("classifier", pipe.getMLPipeline().toString());
		data.put("time_train", (int) timeToCompute);
		data.put("time_predict", -1);
		this.adapter.insert(this.evalTable, data);
	}

}
