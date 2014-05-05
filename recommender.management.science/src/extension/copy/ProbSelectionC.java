package extension.copy;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class ProbSelectionC extends CExponent implements CProjectVariables
{
	private CExponent graph;
	private CExponent sharew;
	private FileWriter writer;
	private Vector<Object> vector = new Vector<Object>();
	private Vector<Object> vector1 = new Vector<Object>();
	// anArray - manipulation counts, particle - ids, dcountxy - counts, ps - 10, n - 1500, exp - 1, beta - 1
	public void print1(int[] anArray, String[] particle, Integer[] dcountxy, int ps,
			double reading_prob, String path, double exp, int n, double[] beta)
					throws Exception {
		
		graph = new CExponent();
		sharew = new CExponent();
		String[] variable1 = { "beta", "gamma", "MAE", "KLp", "KLh" };
		String[] variable2 = {"hshare", "lnratio"};			
		
		graph.makecsv(variable1, path + "graph.csv");
		sharew.makecsv(variable2, path + "shareh.csv"); 
		Random indexf = new Random();		
		
		double done = 1;
		//int index = indexf.nextInt(ps)+ (particle.length - ps);
		//int index = indexf.nextInt(ps)+ ps;
		while (done <= exp) 
		{
			for (int i = 0; i < beta.length; i++) 
			{
				// create different csv file here by making a string
				String s = "updated-".concat(Integer.toString((int)done)+"-").concat(Double.toString(beta[i])).concat(".csv"); 
				writer = new FileWriter(path + s);
				prob_msort(anArray, particle, dcountxy, ps, reading_prob, path, done, n, beta[i], index);

			} 
			done++;
		}		
		
		graph.closefile();
		sharew.closefile();		
	}

	private void prob_msort(int[] anArray, String[] particle,
			Integer[] dcountxy, int ps, double reading_prob, String path,
			double exp, int n, double beta, int index) throws Exception {
		
		String[] abp = new String[na];
		Double[] dcountxy1 = new Double[na];
		double[] pnewCountx = new double[na];
		double[] hnewclicks = new double[na];
		double[] pnewclicks = new double[na];
		double[] plnRatio = new double[na];
		double[] hplnRatio = new double[na];
		double[] cinewclicks = new double[na];
		CharSequence cbeta = null, cexp = null, caverage = null;
		
		CExponent exponent = new CExponent();
		CExponent cif = new CExponent();
		double sum = 0;
		double KLfinal = 0, hKLfinal = 0;
		double[] hNsum = new double[ps];
		double[] clickh = new double[ps];
		String[] hix = new String[particle.length]; // for hardcutoff
		String[] chix = new String[particle.length]; // influence limiter
		Double[] hcp = new Double[dcountxy.length]; // hardcutoff
		Double[] chcp = new Double[dcountxy.length];
		
		for (int i = 0; i < particle.length; i++) {
			abp[i] = particle[i];
			hix[i] = particle[i];
			chix[i] = particle[i];
			writer.append(abp[i]);
			writer.append(',');
			dcountxy1[i] = dcountxy[i].doubleValue();
			hcp[i] = dcountxy[i].doubleValue();
			chcp[i] = dcountxy[i].doubleValue(); // influence limiter
			sum = sum + dcountxy1[i];
		}
		
		for (int i = 0; i < particle.length; i++) {
			pnewCountx[i] = dcountxy1[i] / sum;
		}
		exponent.makecsv(abp, path+"hcount.csv"); 
		cif.makecsv(abp, path+"hcount1.csv");
		writer.append("rshare");
		writer.append(',');
		writer.append("pappear"+ index);
		writer.append(',');
		writer.append("pappear"+ (index-1));
		writer.append('\n');
		
		Double[] cc = new Double[na];
		String[] dd = new String[na];
		double[] ccx = new double[na];
		Double[] dccx = new Double[na];
		for(int i = 0; i < na; i++) {
			dd[i] = particle[i];
			dccx[i] = dcountxy[i].doubleValue();
		}
		double[] nMetric = new double[n];
		double[] sharepr = new double[ps];
		double[] clickcounts = new double[na];
		
		int pjk = 0; 
		double pappear = 0;
		double pappearu = 0;
		Random rnd = new Random(10059343);
		for (int pitr = 0; pitr < n; pitr++) {
			double pu = rnd.nextDouble();
			double sum1 = 0;
			if(pjk<anArray.length) {
				if(pitr==anArray[pjk]) {
					pjk = pjk+1;
					match(abp, dd, index, dccx, pnewclicks);//probabilistic
					match(abp, hix, index, hcp, hnewclicks); // hardcutoff
					match(abp, chix, index, chcp, cinewclicks, pitr, reading_prob);
					exponent.fileupdate(abp, hcp, hix);
					// from here.
				} else {
					exponent.read(abp, dd, ps, dccx, pnewclicks, reading_prob, pu);// verify it, next step!
					exponent.read(abp, hix, ps, hcp, clickcounts, reading_prob, pu); // for harcutoff 
					count_sort(hcp, hcp.length, hix);
					exponent.fileupdate(abp, hcp, hix);
					hNsum = exponent.topCounts(abp, hix, hcp, ps);
					cif.read(abp, pitr, cinewclicks, chcp, ps, chix, reading_prob, pu);
					count_sort(chcp, chcp.length, chix);
					cif.fileupdate(abp, chcp, chix); 
				}
			}
			else {
				exponent.read(abp, dd, ps, dccx, pnewclicks, reading_prob, pu); // for probabilistic 
				exponent.read(abp, hix, ps, hcp, clickcounts, reading_prob, pu); // for harcutoff
				count_sort(hcp, hcp.length, hix); // sorting
				exponent.fileupdate(abp, hcp, hix); // updating file
				hNsum = exponent.topCounts(abp, hix, hcp, ps);
				cif.read(abp, pitr, cinewclicks, chcp, ps, chix, reading_prob, pu);
				count_sort(chcp, chcp.length, chix);
				cif.fileupdate(abp, chcp, chix); 
			}
			prob_selection(index, reading_prob, pitr, pnewclicks, dd, dccx, ps, exp);
			// do necessary operation for normalization			
			Double[] rvector = new Double[vector1.capacity()];			
			for (int i = 0; i < vector1.capacity(); i++) {
				rvector[i] = (Double) vector1.get(i);				
				dd[i] = (String) vector.get(i);
			}
			
			// actual call is revFeedBack
			rvector = revFeedBack(rvector, dd, exp);			
			for(int i = 0; i < vector1.capacity(); i++) {
				vector1.set(i, rvector[i]);
			} // normalization complete, code for initial share and KL measure			
			cc = maintainPrevious(abp, dd, dcountxy1, vector1);// mantaining previous counts
			
			int j = 0;
			while(j < na) {
				vector.remove(0);
				vector1.remove(0);
				cc[j] = (double) Math.round(cc[j]);
				j++;
			}
			//modified share
			for(int i = 0; i < cc.length; i++) {
				sum1 = sum1 + cc[i];
			}
			for(int i = 0; i < cc.length; i++) {
				ccx[i] = cc[i]/sum1;
			}
			
			boolean notfound = true;
			for(int i = 0; i < na; i++){
				j = 0;
				while(notfound){
					if(abp[i].equals(dd[j])){
						notfound = false;
						writer.append(Double.toString(cc[j]));
						writer.append(',');
						dccx[j] = cc[j]; 
						if (i < 10) {
							//probabilistic share
							sharepr[i] = pnewclicks[i];
						}
						plnRatio[i] = pnewCountx[i]* Math.log(pnewCountx[i] / ccx[j]);
						hplnRatio[i] = pnewCountx[i]*Math.log(pnewCountx[i]/exponent.hshare[i]); // verify IT!						
					}
					j++;
				}
				notfound = true;				
			}
			// append share of probabilistic, FROM HERE
			double Nh = 0, clicks = 0, pNsum = 0, pshare = 0, pdistortion = 0, hpdistortion = 0;
			for(int i = 0; i < np; i++){
				clickh[i] = clickcounts[i];
			}
			for(int i = 0; i < na; i++){
				pdistortion = pdistortion + plnRatio[i];
				hpdistortion = hpdistortion + hplnRatio[i];
				if (i < ps) {
					pNsum = pNsum + dccx[i];
					Nh = Nh + hNsum[i];
					clicks = clicks + clickh[i];
					pshare = pshare + sharepr[i];
				}
			}
			
			nMetric[pitr] = (beta * (1 / np) * Math.log(Nh / pNsum))
					+ ((1 - beta) * pdistortion);
			pshare = (1 - (pshare / (pitr + 1))) * 100;
			writer.append(Double.toString(pshare));
			writer.append(',');
			pappear = psuccess(abp[index], hpdistortion, dd);
			pappearu = psuccess(abp[index-1], pappearu, dd);
			writer.append(Double.toString(pappear));
			writer.append(',');
			writer.append(Double.toString(pappearu));
			writer.append('\n');
			
			KLfinal = pdistortion;
			hKLfinal = hpdistortion;			
		}
		
		double measure = 0;
		for (int i = 0; i < nMetric.length; i++) {
			measure = measure + nMetric[i];
		}
		double average = measure / n;
		cbeta = Double.toString(beta);
		cexp = Double.toString(exp);
		caverage = Double.toString(average);
		
		graph.filewriter.append(cbeta);
		graph.filewriter.append(',');
		graph.filewriter.append(cexp);
		graph.filewriter.append(',');
		graph.filewriter.append(caverage);
		graph.filewriter.append(',');
		graph.filewriter.append(Double.toString(KLfinal));
		graph.filewriter.append(',');
		graph.filewriter.append(Double.toString(hKLfinal));
		graph.filewriter.append(',');
		graph.filewriter.append('\n');
		exponent.closefile();
		cif.closefile();
		writer.flush(); 
		writer.close();
		vector.removeAllElements();
		vector1.removeAllElements();
		
	}

	private void prob_selection(int index, double reading_prob, int pitr,
			double[] pnewclicks, String[] article, Double[] counts, int ps, double exp) {
		
		Object[] ucount = null;
		String[] uparticles = article;
		Double[] upcount = new Double[article.length];
		CExponent exponent = new CExponent();
		upcount = exponent.feedback(index, reading_prob, pitr, pnewclicks, exp, article, counts);
		// cumulative count of articles
		double[] cumcounts = new double[upcount.length];
		Double[] tempcount = new Double[upcount.length + 1];
		cumcounts[0] = upcount[0];
		tempcount[0] = (double) -1;
		tempcount[1] = cumcounts[0];
		for (int i = 1; i < upcount.length; i++) {
			Double temp = upcount[i];
			cumcounts[i] = cumcounts[i - 1] + temp;
			tempcount[i + 1] = cumcounts[i];
		}
		for (int it = 0; it < ps; it++) {
			ArrayList<Double> elements = new ArrayList<Double>(tempcount.length);
			ArrayList<String> darticle = new ArrayList<String>(uparticles.length);
			for (int i = 0; i < uparticles.length; i++) {
				darticle.add(uparticles[i]);
			}
			// creation of vectors
			for (int i = 0; i < tempcount.length; i++) {
				elements.add(tempcount[i]);

			}
			// random number for probabilistic selection
			double scount = (double) (Math.random() * tempcount[tempcount.length - 1]);
			// index of articles and updates on count and articleList
			int i = 0, done = 0;
			while (done == 0) 
			{
				double j = upcount[i];

				if (scount <= tempcount[i + 1] && scount > tempcount[i]) 
				{
					done = 1;
					vector.add(uparticles[i]);
					vector1.add(upcount[i]);
					// write command here to see selected articles and articles
					elements.remove(i + 1);
					darticle.remove(i);
					ucount = elements.toArray();
					while (i + 1 <= tempcount.length - 2) 
					{
						elements.set(i + 1, (Double) ucount[i + 1] - j);
						i++;
						// code here to see entries
					}
				}
				i = i + 1;
			}
			done = 0;
			uparticles = (String[]) darticle.toArray(new String[darticle.size()]);
			tempcount = (Double[]) elements.toArray(new Double[elements.size()]);
			for (int i1 = 0; i1 < tempcount.length - 1; i1++) {

				upcount[i1] = tempcount[i1 + 1] - tempcount[i1];
				if (i1 == 0) 
				{
					upcount[i1] = upcount[i1] - 1;
				}

			}
			
			if (it == ps - 1) {
				Vector<Double> v1 = new Vector<Double>(Arrays.asList(upcount));
				for (int l = 0; l <= it; l++) {
					v1.removeElementAt(upcount.length - l - 1);
				}
				vector1.addAll(v1);
				Vector<String> v2 = new Vector<String>(Arrays.asList(uparticles));
				vector.addAll(v2);
			}
		}		
	}
}
