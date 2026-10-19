public class UploadAsyncTask extends AsyncTask {

    protected String doInBackground() {

        return new UploadClient().uploadFile();

    }

    protected void onPostExecute(String result) {

        System.out.println(result);

    }

}