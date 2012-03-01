/**
 * Copyright (c) 2011, The University of Southampton and the individual contributors.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted provided that the following conditions are met:
 *
 *   * 	Redistributions of source code must retain the above copyright notice,
 * 	this list of conditions and the following disclaimer.
 *
 *   *	Redistributions in binary form must reproduce the above copyright notice,
 * 	this list of conditions and the following disclaimer in the documentation
 * 	and/or other materials provided with the distribution.
 *
 *   *	Neither the name of the University of Southampton nor the names of its
 * 	contributors may be used to endorse or promote products derived from this
 * 	software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.openimaj.ml.pca;

import java.util.Collection;

import org.openimaj.feature.FeatureVector;
import org.openimaj.math.matrix.algorithm.pca.PrincipalComponentAnalysis;

import Jama.Matrix;

public class FeatureVectorPCA extends PrincipalComponentAnalysis {
	PrincipalComponentAnalysis inner; 
	
	public FeatureVectorPCA(PrincipalComponentAnalysis inner) {
		this.inner = inner;
	}
	
	public void learnBasis(FeatureVector[] data) {
		double [][] d = new double[data.length][];
		
		for (int i=0; i<data.length; i++) {
			d[i] = data[i].asDoubleVector();
		}
		
		learnBasis(d);
	}
	
	public void learnBasis(Collection<FeatureVector> data) {
		double [][] d = new double[data.size()][];
		
		int i=0;
		for (FeatureVector fv : data) {
			d[i++] = fv.asDoubleVector();
		}
		
		learnBasis(d);
	}
	
//	public DoubleFV project(FeatureVector vector) {
//		return new DoubleFV(project(vector.asDoubleVector()));
//	}

	@Override
	public void learnBasis(double[][] data) {
		inner.learnBasis(data);
		this.basis = inner.getBasis();
		this.eigenvalues = inner.getEigenValues();
		this.mean = inner.getMean();
	}

	@Override
	protected void learnBasisNorm(Matrix norm) {
		inner.learnBasis(norm);
	}
}
