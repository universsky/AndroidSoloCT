package universsky.solo.report;

import java.io.IOException;
import java.net.MalformedURLException;

import universsky.solo.util.Const;

public class GenerateReport {

    /**
     * @param args
     *            void main
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

	String run_stamp = "20140421122151";
	genReport(run_stamp);

    }

    public static void report(long start, long end) {

	System.out.println("Running Time: " + (end - start) / 1000 + " s"
		+ "( " + (end - start) / 1000 / 60 + " min )");
	System.out.println("Run timestamp:" + Const.timestamp);

	String runningSec = (end - start) / 1000 + "";
	String runningMin = (end - start) / 1000 / 60 + "";

	GenerateReport.genReport(Const.timestamp, runningSec, runningMin);

    }

    public static void genReport(String run_stamp) throws IOException {

	if (java.awt.Desktop.isDesktopSupported()) {
	    try {
		String reportUriStr = Const.reportPath + run_stamp;
		java.net.URI reportUri = java.net.URI.create(reportUriStr);

		java.awt.Desktop dp = java.awt.Desktop.getDesktop();
		if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
		    dp.browse(reportUri);
		}

	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }

    public static void genReport(String run_stamp, String runningSec,
	    String runningMin) {

	// ä¯ÀÀÆ÷´ò¿ª
	if (java.awt.Desktop.isDesktopSupported()) {
	    try {
		String reportUriStr = Const.reportPath + run_stamp + "&sec="
			+ runningSec + "&min=" + runningMin;
		java.net.URI reportUri = java.net.URI.create(reportUriStr);
		java.awt.Desktop dp = java.awt.Desktop.getDesktop();
		if (dp.isSupported(java.awt.Desktop.Action.BROWSE)) {
		    dp.browse(reportUri);
		}
	    } catch (Exception e) {
		e.printStackTrace();

	    }
	}

    }
}
