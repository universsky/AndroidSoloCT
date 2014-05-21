/**
 * etao.test FileUpload.java 2014��4��20��
 */
package universsky.solo.report;

/**
 * @author �����¹⽣
 * 2014��4��20�� ����5:03:58
 */

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FileUpload {

    /**
     * ��������
     * 
     * @param url
     *            �����ַ
     * @param filePath
     *            �ļ�·��
     * @return
     * @throws IOException
     */
    public synchronized String send(String url, String filePath)
	    throws IOException {

	File file = new File(filePath);
	if (!file.exists() || !file.isFile()) {
	    return "ERROR";
	}

	/**
	 * ��һ����
	 */
	URL urlObj = new URL(url);
	HttpURLConnection con = (HttpURLConnection) urlObj.openConnection();

	/**
	 * ���ùؼ�ֵ
	 */
	con.setRequestMethod("POST"); // ��Post��ʽ�ύ����Ĭ��get��ʽ
	con.setDoInput(true);
	con.setDoOutput(true);
	con.setUseCaches(false); // post��ʽ����ʹ�û���

	// ��������ͷ��Ϣ
	con.setRequestProperty("Connection", "Keep-Alive");
	con.setRequestProperty("Charset", "UTF-8");

	// ���ñ߽�
	String BOUNDARY = "----------" + System.currentTimeMillis();
	con.setRequestProperty("Content-Type", "multipart/form-data; boundary="
		+ BOUNDARY);

	// ����������Ϣ

	// ��һ���֣�
	StringBuilder sb = new StringBuilder();
	sb.append("--"); // ////////�����������
	sb.append(BOUNDARY);
	sb.append("\r\n");
	sb.append("Content-Disposition: form-data;name=\"file\";filename=\""
		+ file.getName() + "\"\r\n");
	sb.append("Content-Type:application/octet-stream\r\n\r\n");

	byte[] head = sb.toString().getBytes("utf-8");

	// ��������

	OutputStream out = new DataOutputStream(con.getOutputStream());
	out.write(head);

	// �ļ����Ĳ���
	DataInputStream in = new DataInputStream(new FileInputStream(file));
	int bytes = 0;
	byte[] bufferOut = new byte[1024];
	while ((bytes = in.read(bufferOut)) != -1) {
	    out.write(bufferOut, 0, bytes);
	}
	in.close();

	// ��β����
	byte[] foot = ("\r\n--" + BOUNDARY + "--\r\n").getBytes("utf-8");// ����������ݷָ���

	out.write(foot);

	out.flush();
	out.close();

	/**
	 * ��ȡ��������Ӧ�������ȡ,�����ύ���ɹ�
	 */

	return " Upload | " + filePath + " | " + con.getResponseCode() + " | "
		+ con.getResponseMessage();

	/**
	 * ����ķ�ʽ��ȡҲ�ǿ��Ե�
	 */

	// try {
	// // ����BufferedReader����������ȡURL����Ӧ
	// BufferedReader reader = new BufferedReader(new InputStreamReader(
	// con.getInputStream()));
	// String line = null;
	// while ((line = reader.readLine()) != null) {
	// System.out.println(line);
	// }
	// } catch (Exception e) {
	// System.out.println("����POST��������쳣��" + e);
	// e.printStackTrace();
	// }

    }

    public static void main(String[] args) throws IOException {
	FileUpload up = new FileUpload();
	for (int i = 1; i <= 22; i++) {
	    System.out.println(up.send(
		    "http://127.0.0.1:8888/UploadServlet/upload",
		    "D:\\AAAATEMP\\img\\W (" + i + ").jpg"));
	}
    }
}
