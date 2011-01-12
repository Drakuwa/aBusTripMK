package org.andnav.osm.views.util;

//import com.app.busmk.OpenSatNavConstants;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class HttpUserAgentHelper {
	
	private static String ua = null;
	
	public static String getUserAgent(Context context) {
		if (ua!=null)
			return ua;
		try {
			// Read package name and version number from manifest
			PackageManager manager = context.getPackageManager();
			PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
			ua = info.packageName + " " + info.versionName;

		} catch (NameNotFoundException e) {
			//Log.e(OpenSatNavConstants.LOG_TAG, "Unable to define the user agent string", e);
		}
		return ua;
	}
}
