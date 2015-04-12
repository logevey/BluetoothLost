package com.doutech.bluetoothlost.utils;

public class RssitoDistance {
	
	
	//public double[] nrssi = new double[100];
	
	public double[] Gass_L = {
			0.0312, 0.0359, 0.0407, 0.0454, 0.0499,	0.0540,	0.0574,
			0.0602,	0.0621,	0.0631,	0.0631, 0.0621,	0.0602,	0.0574,
			0.0540, 0.0499, 0.0454,	0.0407,	0.0359,	0.0312
			}; 
	
	public double[] ELL_F = {
			 -0.0059, -0.0059, -0.0032,  -0.0019, -0.0006, 0.0006, 0.0021, 0.0039, 0.0060, 0.0085,
			  0.0113,  0.0143,  0.0175,   0.0210,  0.0245, 0.0281, 0.0316, 0.0350, 0.0381, 0.0410,
			  0.0436,  0.0457,  0.0473,   0.0484,  0.0490, 0.0490, 0.0484, 0.0473, 0.0457, 0.0436,
			  0.0410,  0.0381,  0.0350,   0.0316,  0.0281, 0.0245, 0.0210, 0.0175, 0.0143, 0.0113,
			  0.0085,  0.0060,  0.0039,   0.0021,  0.0006,-0.0006,-0.0019,-0.0032,-0.0059, -0.0059
			 };
	
	public double rssitodis(double[] nrssi) {
		
	

		int i;
		for (i = 0; i < nrssi.length; i++) {
			
			//System.out.println("the value of nrssi  " + i + "  is :" + nrssi[i]);
		}

		int m = nrssi.length;
		
		System.out.println("the nrssi's length is ..." + m);

		int n = Gass_L.length;

		double[] nrssi_Gass = new double[m + n - 1];

		nrssi_Gass = conv(nrssi, Gass_L, m, n);
		// System.out.println("the nrssi_Gass's length is ..."+nrssi_Gass.length);

		for (i = 0; i < nrssi_Gass.length; i++) {
			// System.out.println("the value of nrssi_Gass  "+i+"  is :"+nrssi_Gass[i]);
		}

		double[] nrssi_Gass_cut = new double[m - n + 1];
		nrssi_Gass_cut = cut(nrssi_Gass, n);
		System.out.println("the nrssi_Gass_cut's length is ..."
				+ nrssi_Gass_cut.length);
		for (i = 0; i < nrssi_Gass_cut.length; i++) {
			// System.out.println("the value of nrssi_Gass_cut  "+i+
			// "  is   "+nrssi_Gass_cut[i]);
		}

		double[] nrssi_distance = new double[m - n + 1];
		nrssi_distance = rssi2dis(nrssi_Gass_cut);
		for (i = 0; i < nrssi_distance.length; i++) {
			// System.out.println("the value of nrssi_distance   "+i+"  is   "+nrssi_distance[i]);
		}

		int k = ELL_F.length;
		int l = nrssi_distance.length;
		double[] nrssi_dis_filter = new double[l + k - 1];
		nrssi_dis_filter = conv(nrssi_distance, ELL_F, l, k);
		for (i = 0; i < nrssi_dis_filter.length; i++) {
			// System.out.println("the value of nrssi_dis_filter   "+i+"  is   "+nrssi_dis_filter[i]);
		}

		double[] nrssi_dis_filter_cut = new double[l - k + 1];
		nrssi_dis_filter_cut = cut(nrssi_dis_filter, k);

		for (i = 0; i < nrssi_dis_filter_cut.length; i++) {
//			System.out.println("the value of nrssi_dis_filter_cut   " + i
//					+ "  is   " + nrssi_dis_filter_cut[i]);
		}

		double rssi_final = 0;
		for (i = 0; i < nrssi_dis_filter_cut.length; i++) {
			rssi_final += nrssi_dis_filter_cut[i];
		}

		rssi_final = rssi_final / (double) (nrssi_dis_filter_cut.length);

		System.out.println("the final value of rssi is ..." + rssi_final);

		return rssi_final;

	}
	
	 private final double[] cut (double u[], int m)
	    {
	    	
	    	int n = u.length;
	    	int i = 0;
	    	int j = 0;
	    	double[] v = new double[n-m-m+2];
	    	
	    	for(i=m-1;i<n-m+1;i++)
	    	{
	    		v[j]=u[i];
	    		j++;
	    	}
	    	
	    	return v;
	    }
	 
	 
	
	private final double[] conv (double u[],double v[],int m,int n)
	{
		
		int i,j;
		int k = m+n-1;
		
		double[] w = new double[k];
		
		for(i=m-1;i>=0;i--)
		{
			for(j=n-1;j>=0;j--)
			{
				w[i+j] += u[i]*v[j];
				//System.out.println("the w["+i+j+"]...."+w[i+j]+"\n");
			}
		
		}
		return w;
	}
	
	 private final double[] rssi2dis(double u[])
	   {
		   
		   double d = 2.3;
		   double A = -32;
		   double n = 2;
		   
		   int m = u.length;
		   int i = 0;
		   double l;
		  
		   for(i=0;i<m;i++)
		   {
			   l=(A-u[i])/(10*n);
			   u[i]=Math.pow(10, l);
		   }
		   
		   return u;
	   }
	    

}
