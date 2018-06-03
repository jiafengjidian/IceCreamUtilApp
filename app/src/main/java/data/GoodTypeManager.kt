package data

object GoodTypeManager
{
    var PositionX = 0
    var PositionY = 0

    private val mGoodTypeList = ArrayList<GoodType>()

    init
    {
        val origin = GoodType.Build().apply {
            x = 518
            y = 518
            name = "原点"
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
            x = PositionX
            y = PositionY
        }.build()
        add(p)
    }

    fun getCounter() = mGoodTypeList.size

}