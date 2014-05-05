package extension;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

public class ProbSelection {
	
	private int done;
	private FileWriter writer;
	private FileWriter writer1;	
	public FileWriter sharew;
	String filepath;

	public Vector<Object> vector = new Vector<Object>();
	public Vector<Object> vector1 = new Vector<Object>();
	Exponent exponent = new Exponent();
	ManipulationCount mcount = new ManipulationCount();
	CountInfluence cif = new CountInfluence();
	InfluenceLimiter appear = new InfluenceLimiter();
	private giniInequality inequality = new giniInequality();
	double gexp, denom = 1;
	boolean one = true;

	String[] gindices = new String[200];

	private void prob_selection(int index, double reading_prob, int pitr, double[] newclick, String[] article, Double[] counts, int n,
			double exp) 
	{		
		Object[] ucount = null;
		// copy of variables
		String[] uparticles = article;
		Double[] upcount = new Double[article.length];
		gexp = exp;
		upcount = exponent.feedBack(index, reading_prob, pitr, newclick, gexp, denom, article, counts);
		// cumulative count of articles
		double[] cumcounts = new double[upcount.length];
		Double[] tempcount = new Double[upcount.length + 1];
		cumcounts[0] = upcount[0];
		tempcount[0] = (double) -1;
		tempcount[1] = cumcounts[0];
		for (int i = 1; i < upcount.length; i++) 
		{
			Double temp = upcount[i];
			cumcounts[i] = cumcounts[i - 1] + temp;
			tempcount[i + 1] = cumcounts[i];
		}

		for (int it = 0; it < n; it++) 
		{

			ArrayList<Double> elements = new ArrayList<Double>(tempcount.length);
			ArrayList<String> darticle = new ArrayList<String>(uparticles.length);

			for (int i = 0; i < uparticles.length; i++) 
			{
				darticle.add(uparticles[i]);
			}

			// creation of vectors

			for (int i = 0; i < tempcount.length; i++) 
			{
				elements.add(tempcount[i]);

			}
			// random number for probabilistic selection
			double scount = (double) (Math.random() * tempcount[tempcount.length - 1]);
			// index of articles and updates on count and articleList
			int i = 0;
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
			}// end while loop
			done = 0;
			// updated count and articles
			uparticles = (String[]) darticle.toArray(new String[darticle.size()]);
			tempcount = (Double[]) elements.toArray(new Double[elements.size()]);
			for (int i1 = 0; i1 < tempcount.length - 1; i1++) 
			{

				upcount[i1] = tempcount[i1 + 1] - tempcount[i1];
				if (i1 == 0) 
				{
					upcount[i1] = upcount[i1] - 1;
				}

			}

			if (it == n - 1) 
			{
				Vector<Double> v1 = new Vector<Double>(Arrays.asList(upcount));
				for (int l = 0; l <= it; l++) 
				{
					v1.removeElementAt(upcount.length - l - 1);
				}

				vector1.addAll(v1);
				Vector<String> v2 = new Vector<String>(Arrays.asList(uparticles));
				vector.addAll(v2);

			}

		}
	}

	public void prob_msort(int[] anArray, String[] particle, Integer[] dcountxy, int ps,
			double reading_prob, String path, double exp, int n, double beta, int index)
					throws IOException 
					{
		// Initial allocation of articles
		String[] abp = new String[particle.length];
		Double[] dcountxy1 = new Double[dcountxy.length];
		double[] pnewCountx = new double[particle.length];
		double[] newclicks = new double[particle.length];
		double[] cinewclicks = new double[particle.length];
		double[] plnRatio = new double[particle.length];
		double[] hplnRatio = new double[particle.length];
		CharSequence cbeta = null, cexp = null, caverage = null;

		double sum = 0;
		double KLfinal = 0, hKLfinal = 0;
		double[] hNsum = new double[ps];
		double[] clickh = new double[ps];
		// for hardcutoff
		String[] hix = new String[particle.length];
		String[] chix = new String[particle.length];
		Double[] hcp = new Double[dcountxy.length];
		Double[] chcp = new Double[dcountxy.length];

		for (int i = 0; i < particle.length; i++) 
		{
			abp[i] = particle[i];
			hix[i] = particle[i];
			chix[i] = particle[i];
			gindices[i] = particle[i];
			hcp[i] = dcountxy[i].doubleValue();
			chcp[i] = dcountxy[i].doubleValue();
			dcountxy1[i] = dcountxy[i].doubleValue();
			sum = sum + dcountxy1[i];
		}

		// first row of excel
		// writer = new FileWriter(path + "Updated.csv");

		for (int j = 0; j < abp.length; j++) 
		{
			writer.append(abp[j]);
			//exponent.makecsv(abp);
			writer.append(',');

			pnewCountx[j] = dcountxy1[j] / sum;
			newclicks[j] = 0;
			cinewclicks[j] = 0;
		}
		exponent.makecsv(abp);
		cif.makeFile(abp);
		// extra column for share
		writer.append("rshare");
		writer.append(',');
		writer.append("pappear"+ index);
		writer.append(',');
		writer.append("pappear"+ (index-1));
		writer.append('\n');


		//Random pGenerator = new Random();
		Double[] cc = new Double[dcountxy.length];
		String[] dd = new String[particle.length];
		double[] ccx = new double[dcountxy.length];
		Double[] dccx = new Double[dcountxy.length];
		double[] nMetric = new double[n];
		double[] sharepr = new double[ps];
		double psx = new Double(ps);
		inequality.makecsv();

		int pjk = 0; 
		double pappear = 0;
		double pappearu = 0;
		for (int pitr = 0; pitr < n; pitr++) 
		{			
			double sum1 = 0;
			//    double pu = Math.random();
			if (pitr == 0) 
			{
				prob_selection(index, reading_prob, pitr, newclicks, particle, dcountxy1, ps, exp);
				hNsum = exponent.count_selection(hcp, ps, hix, reading_prob, abp);
				cif.count_iselection(pitr, cinewclicks, chcp, ps, chix, reading_prob, abp);

			} 
			else 
			{
				if(pjk<anArray.length)
				{
					// System.out.println(",, ");
					if(pitr==anArray[pjk])
					{
						// call a method to match
						pjk = pjk+1;
						mcount.match(dd, index, dccx, newclicks); //probabilistic
						mcount.match(hix, index, hcp, newclicks); // hardcutoff
						cif.discountMatch(chix, index, chcp, cinewclicks, pitr, reading_prob);
						exponent.fileupdate(hcp, hix, abp);
						//prob_selection(index, reading_prob, pitr, newclicks, dd, dccx, ps, exp);
						//System.out.print(newclicks[index]);
					}
					else
					{
						mcount.read(reading_prob, ps, dccx, abp, dd, newclicks); // probabilistic
						hNsum = exponent.count_selection(hcp, ps, hix, reading_prob, abp); // hardcutoff
						// method to implement reading behavior
						cif.count_iselection(pitr, cinewclicks, chcp, ps, chix, reading_prob, abp);
					}
				}
				else if(pjk == anArray.length)
				{
					mcount.read(reading_prob, ps, dccx, abp, dd, newclicks); // probabilistic
					hNsum = exponent.count_selection(hcp, ps, hix, reading_prob, abp); // hardcutoff
					// impelement reading behavior
					cif.count_iselection(pitr, cinewclicks, chcp, ps, chix, reading_prob, abp);
				}
				prob_selection(index, reading_prob, pitr, newclicks, dd, dccx, ps, exp);

			}
			// prob_selection(dd, cc, ps, exp);		    

			// do necessary operation for normalization
			Double[] rvector = new Double[vector1.capacity()];
			for (int i = 0; i < vector1.capacity(); i++) 
			{
				rvector[i] = (Double) vector1.get(i);
				dd[i] = (String) vector.get(i);// code has been changed here
			}

			// actual call is revFeedBack
			//rvector = exponent.feedBack(1 / gexp, 1 / Math.pow(denom, gexp), rvector);
			rvector = exponent.revFeedBack(rvector, dd, exp);
			for (int j = 0; j < vector1.capacity(); j++) 
			{
				vector1.set(j, rvector[j]);
			}
			// normalization complete
			// write code for initial share and KL measure

			for (int i = 0; i < particle.length; i++) 
			{
				cc[i] = (Double) vector1.get(i);
				//dd[i] = (String) vector.get(i);

				// code for keeping the earlier count if 0
				if (cc[i] == 0) 
				{
					int donep = 0;
					int i11 = 0;
					while (donep == 0) 
					{
						if (dd[i].equals(abp[i11].toString())) 
						{
							donep = 1;
							cc[i] = dcountxy1[i11];
						}
						i11 = i11 + 1;
					}
				}
				// code finished

			}

			int j = 0;
			while (j < particle.length) 
			{
				vector.remove(0);
				vector1.remove(0);
				cc[j] = (double) Math.round(cc[j]);
				j++;
			}

			// modified share and round the counts

			for (int i = 0; i < cc.length; i++) 
			{
				sum1 = sum1 + cc[i];

			}

			
			for (int i = 0; i < cc.length; i++) 
			{
				ccx[i] = cc[i] / sum1;				
			}			

			int donep = 0;
			for (int i = 0; i < abp.length; i++) 
			{
				int i1 = 0;
				while (donep == 0) 
				{
					if (abp[i].equals(dd[i1].toString())) 
					{
						donep = 1;
						writer.append(Double.toString(cc[i1])); // for actual count print cc[i]
						writer.append(',');
						// new share
						dccx[i1] = cc[i1]; 
						if (i < 10) 
						{
							//probabilistic share
							sharepr[i] = newclicks[i];
						}

						plnRatio[i] = pnewCountx[i]* Math.log(pnewCountx[i] / ccx[i1]);
						hplnRatio[i] = pnewCountx[i]*Math.log(pnewCountx[i]/exponent.hShare[i]); 

					}
					i1 = i1 + 1;
				}
				donep = 0;

			}

			// append share of probabilistic

			double Nh = 0, pdistortion = 0, hpdistortion = 0, pNsum = 0, pshare = 0, clicks = 0;
			//hNsum = exponent.count_selection(hcp, ps, hix, reading_prob, abp);
			// to write gini coefficient.
			inequality.countPass(cc, hcp);
			clickh = exponent.clickcounts; //for hardcutoff
			for (int i = 0; i < abp.length; i++) 
			{
				pdistortion = pdistortion + plnRatio[i];
				hpdistortion = hpdistortion + hplnRatio[i];
				if (i < ps) 
				{
					pNsum = pNsum + dccx[i];
					Nh = Nh + hNsum[i];
					clicks = clicks + clickh[i];
					pshare = pshare + sharepr[i];
				}
			} 

			/* nMetric[pitr] = (beta * (1 / psx) * Math.log(Nh / pNsum))
					+ ((1 - beta) * pdistortion); */
			nMetric[pitr] = Math.abs((Nh - pNsum)/Nh);
			
			pshare = (1 - (pshare / (pitr + 1))) * 100;
			writer.append(Double.toString(pshare));
			// code has been modified here
			writer.append(',');
			pappear = appear.psuccess(abp[index], pappear, dd);
			pappearu = appear.psuccess(abp[index-1], pappearu, dd);
			writer.append(Double.toString(pappear));
			writer.append(',');
			writer.append(Double.toString(pappearu));
			writer.append('\n');

			if (one) 
			{
				sum = sum + 1;
				double restclicks = (1 - clicks / (pitr + 1)) * 100;
				double mratio = exponent.ratio;
				String cshare = Double.toString(restclicks);
				String cratio = Double.toString(mratio);
				/*
				sharew.append(cshare);
				sharew.append(',');
				sharew.append(cratio);
				sharew.append('\n');
				*/
			}
			
			KLfinal = pdistortion;
			hKLfinal = hpdistortion;		

		}
		
		
		System.out.println(" \nthe new clicks\n"); 
		for(int l = 0; l < cinewclicks.length; l++) {
			System.out.print(Double.toString(cinewclicks[l]) + ", ");
		}
		
		one = false;

		double measure = 0;
		for (int i = 0; i < nMetric.length; i++) 
		{
			measure = measure + nMetric[i];
		}
		double average = measure / n;
		// increase total count

		cbeta = Double.toString(beta);
		cexp = Double.toString(exp);
		caverage = Double.toString(average);

		writer1.append(cbeta);
		writer1.append(',');
		writer1.append(cexp);
		writer1.append(',');
		writer1.append(caverage);
		writer1.append(',');
		// write KL metric for probabilistic
		writer1.append(Double.toString(KLfinal));
		writer1.append(',');
		// write KL metric for hardcutoff
		writer1.append(Double.toString(hKLfinal));
		writer1.append(',');
		writer1.append('\n');

		writer.flush();
		writer.close();
		exponent.closefile();
		cif.closeit();
		inequality.closefile();
		// close file here!

		vector.removeAllElements();
		vector1.removeAllElements();

					}

	// anArray - manipulation counts, particle - ids, dcountxy - counts, ps - 10, n - 1500, exp - 1, beta - 1
	public void print1(int[] anArray, String[] particle, Integer[] dcountxy, int ps,
			double reading_prob, String path, double exp, int n, double[] beta)
					throws IOException 
					{
		// second excel file
		writer1 = new FileWriter(path + "graph.csv");
		sharew = new FileWriter(path + "shareh.csv");	
		filepath = path;

		String[] variable = { "beta", "gamma", "MAE", "KLp", "KLh" }; 

		for (int i = 0; i < variable.length; i++) 
		{
			writer1.append(variable[i]);
			writer1.append(',');
		}

		sharew.append("hshare");
		sharew.append(',');
		sharew.append("lnratio");
		sharew.append('\n');
		writer1.append('\n');

		double done = 1;
		int index = 10; //index of article to be manipulated
		Random indexf = new Random();
		//int index = indexf.nextInt(ps)+ (particle.length - ps);
		//int index = indexf.nextInt(ps)+ ps;
		System.out.println("\n" + index);
		while (done <= exp) 
		{
			for (int i = 0; i < beta.length; i++) 
			{
				// create different csv file here by making a string
				String s = "updated-".concat(Double.toString(done)).concat(Double.toString(beta[i])).concat(".csv"); 
				writer = new FileWriter(path + s);
				prob_msort(anArray, particle, dcountxy, ps, reading_prob, path, done, n, beta[i], index);

			}

			for (int j = 0; j < 10; j++) 
			{
				exponent.clickcounts[j] = 0;
			}
			done = done + 1.0;
			//System.out.println(exponent.amodified[index]);
		}
		// print share for different values of gamma
		writer1.flush();
		writer1.close();

		sharew.flush();
		sharew.close();
		}
}
