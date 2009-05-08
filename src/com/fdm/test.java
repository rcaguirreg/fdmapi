package com.fdm;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		
		FDM_Remote fdmCon = new FDM_Remote("server", 9091,"admin","admin");
	
		System.out.print(fdmCon.getCompletedDownloads());
	}

}
