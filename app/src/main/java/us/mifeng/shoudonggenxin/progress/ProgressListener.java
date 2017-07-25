package us.mifeng.shoudonggenxin.progress;

/**
 * Created by shido on 2017/7/24.
 */

public interface ProgressListener {
    void progress(long bytesRead, long contentLength, boolean done);

}
