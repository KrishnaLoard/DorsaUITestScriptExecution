package com.dorsa.testdeploymentmain;

import com.dorsa.UITestingScript.RunTheAllScript;

public class CallTheTestUIAll {

	public CallTheTestUIAll() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			RunTheAllScript allScript = new RunTheAllScript();
			allScript.runthescriptwithpath("src//TestsuiterunGhostinspector.bat");
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
	}

}
