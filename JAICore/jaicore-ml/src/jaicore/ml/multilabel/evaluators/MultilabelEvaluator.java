package jaicore.ml.multilabel.evaluators;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jaicore.ml.WekaUtil;
import jaicore.ml.evaluation.BasicMLEvaluator;
import meka.classifiers.multilabel.MultiLabelClassifier;
import weka.classifiers.Classifier;
import weka.core.Instances;

@SuppressWarnings("serial")
public abstract class MultilabelEvaluator implements BasicMLEvaluator, Serializable {
	
	private final static Logger logger = LoggerFactory.getLogger(MultilabelEvaluator.class);
	
	private final Random rand;
	private boolean canceled;

	public MultilabelEvaluator(Random r) {
		super();
		this.rand = r;
	}

	public double getErrorRateForRandomSplit(Classifier c, Instances data, double splitSize) throws Exception {
		List<Instances> split = WekaUtil.realizeSplit(data, WekaUtil.getArbitrarySplit(data, rand, splitSize));
		Instances train = split.get(0);
		Instances test = split.get(1);
		logger.info("Split data set with {} items into {}/{}", data.size(), train.size(), test.size());
		return getErrorRateForSplit(c, train, test);
	}
	
	public double getErrorRateForSplit(Classifier c, Instances train, Instances test) throws Exception {
		MultiLabelClassifier cCopy = (MultiLabelClassifier)WekaUtil.cloneClassifier(c);
		cCopy.buildClassifier(train);
		if (test.isEmpty()) {
			logger.error("Test fold is empty! Size of training set: {}", train.size());
		}
		else
			logger.info("Split size is {}/{}", train.size(), test.size());
		double error = loss(cCopy, test);
		return error;
	}
	
	public abstract double loss(MultiLabelClassifier builtClassifier, Instances test) throws Exception;

	public boolean isCanceled() {
		return canceled;
	}

	public void setCanceled(boolean canceled) {
		this.canceled = canceled;
	}
}
