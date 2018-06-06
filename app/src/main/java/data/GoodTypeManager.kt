package data

object GoodTypeManager
{
    private val mGoodTypeList = ArrayList<GoodType>()

    init
    {
        val origin = GoodType.Build().apply {
            x = 518
            y = 518
            name = "原点"
            row = 0
            col = 0
        }.build()
        mGoodTypeList.add(origin)
    }

    fun get(index: Int) = mGoodTypeList[index]

    fun add(info: GoodType) {
        mGoodTypeList.add(info)
    }

    fun add()
    {
        val p = GoodType.Build().apply {
            name = "位置:${getCounter()}"
            x = DeviceStatusManager.getPosition().x
            y = DeviceStatusManager.getPosition().y
            row = 0
            col = 0
        }.build()
        add(p)
    }

    fun getCounter() = mGoodTypeList.size

}