package com.dastanapps.dastanlib.network;//package com.mebelkart.app.Network;
//
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.graphics.Color;
//import android.os.AsyncTask;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.WindowManager;
//import android.widget.LinearLayout;
//import android.widget.LinearLayout.LayoutParams;
//import android.widget.ProgressBar;
//
//import static com.mebelkart.app.Network.HttpProcess.*;
//
//public class IqbalAsyncTask extends AsyncTask<String, Void, String> {
//
//	private IOnTaskCompleted listenr;
//	private int task;
//	private ProgressDialog pb;
//	private boolean progress;
//	private Context ctxt;
//
//	public IqbalAsyncTask(Context ctxt, IOnTaskCompleted listenr,
//			boolean isprogress) {
//		this.listenr = listenr;
//		progress = isprogress;
//		this.ctxt = ctxt;
//
//		pb = new ProgressDialog(ctxt);
//		// pb.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//		// pb.setMessage("Loading Country....");
//		pb.setCanceledOnTouchOutside(false);
//		pb.setCancelable(true);
//		pb.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
//		if (progress) {
//			pb.show();
//		}
//		LinearLayout.LayoutParams lmap = new LinearLayout.LayoutParams(
//				LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//		LinearLayout llv = new LinearLayout(ctxt);
//		llv.setGravity(Gravity.CENTER);
//		ProgressBar pb1 = new ProgressBar(ctxt);
//		pb1.getIndeterminateDrawable().setColorFilter(
//				Color.parseColor("#0099FF"),
//				android.graphics.PorterDuff.Mode.SRC_IN);
//		llv.addView(pb1);
//		pb.setContentView(llv, lmap);
//	}
//
//	public void setTask(int task) {
//		this.task = task;
//	}
//
//	public int getTask() {
//		return task;
//	}
//
//	@Override
//	protected void onPreExecute() {
//		super.onPreExecute();
//	}
//
//	@Override
//	protected String doInBackground(String... params) {
//		return postJson(params[0]);
//	}
//
//	@Override
//	protected void onPostExecute(String result) {
//		super.onPostExecute(result);
//		if (progress) {
//			try {
//				pb.dismiss();
//			} catch (Exception e) {
//				Log.i("IqbalAsyncTaskException ", e.getMessage());
//			}
//		}
//		listenr.onTaskCompleted(task,result);
//	}
//
//}
