package task

import android.os.Handler
import android.os.HandlerThread

object Task
{
    private val mAsyncThread = HandlerThread("async.thread")
    private val mDelayThread = HandlerThread("delay.thread")

    val UiHandler = Handler()
    val AsyncHandler: Handler
    val DelayHandler: Handler

    init
    {
        mAsyncThread.start()
        mDelayThread.start()
        AsyncHandler = Handler(mAsyncThread.looper)
        DelayHandler = Handler(mDelayThread.looper)
    }
}