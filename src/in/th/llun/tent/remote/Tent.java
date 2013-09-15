package in.th.llun.tent.remote;

import in.th.llun.tent.Settings;

import org.apache.http.HttpVersion;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.content.Context;

public class Tent {

	public static final String LOG_TAG = "Tent";

	private static final String BASECAMP_HEADER_CONTACT_VALUE = "Maythee Anegboonlap Integration (null@llun.in.th)";

	private static Tent sTent;
	private Settings mSettings;

	private DefaultHttpClient mClient;
	private HttpContext mClientContext;
	private BasicCookieStore mCookieStore;

	public static Tent getInstance(Context context) {
		if (sTent == null) {
			synchronized (Tent.class) {
				if (sTent == null) {
					sTent = new Tent(Settings.getInstance(context));
				}
			}
		}

		return sTent;
	}

	public Tent(Settings settings) {
		mSettings = settings;

		HttpParams params = new BasicHttpParams();
		ConnManagerParams.setMaxTotalConnections(params, 100);
		HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
		params.setParameter(CoreProtocolPNames.USER_AGENT,
		    BASECAMP_HEADER_CONTACT_VALUE);

		// Set the timeout in milliseconds until a connection is established.
		// The default value is zero, that means the timeout is not used.
		int timeoutConnection = 10000;
		HttpConnectionParams.setConnectionTimeout(params, timeoutConnection);
		// Set the default socket timeout (SO_TIMEOUT)
		// in milliseconds which is the timeout for waiting for data.
		int timeoutSocket = 30000;
		HttpConnectionParams.setSoTimeout(params, timeoutSocket);

		// Create and initialize scheme registry
		SchemeRegistry schemeRegistry = new SchemeRegistry();
		schemeRegistry.register(new Scheme("http", PlainSocketFactory
		    .getSocketFactory(), 80));
		schemeRegistry.register(new Scheme("https", SSLSocketFactory
		    .getSocketFactory(), 443));

		// Create an HttpClient with the ThreadSafeClientConnManager.
		// This connection manager must be used if more than one thread will
		// be using the HttpClient.
		ClientConnectionManager cm = new ThreadSafeClientConnManager(params,
		    schemeRegistry);
		mClient = new DefaultHttpClient(cm, params);
		mClientContext = new BasicHttpContext();
		mCookieStore = new BasicCookieStore();
		mClient.setCookieStore(mCookieStore);

	}

	public boolean isLoggedIn() {
		return false;
	}

}
