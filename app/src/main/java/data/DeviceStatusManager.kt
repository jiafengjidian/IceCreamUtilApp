package data

object DeviceStatusManager
{
    const val ACTION_POSITION_CHANGED = "action.position.changed"
    const val EXTRA_POSITION_X = "extra.position.x"
    const val EXTRA_POSITION_Y = "extra.position.y"

    private var mLocalPosition = Position(-1, -1)

    fun getPosition() = mLocalPosition

    fun updatePosition(x: Int, y: Int): Boolean
    {
        mLocalPosition = Position(x, y)
        return true
    }
}