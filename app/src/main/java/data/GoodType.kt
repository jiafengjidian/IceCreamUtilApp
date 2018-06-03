package data

data class Position(val x: Int, val y: Int)

data class GoodType(val name: String, val position: Position)
{
    class Build
    {
        var x = 0
        var y = 0
        var name: String? = null

        fun build(): GoodType
        {
            return GoodType(name!!, Position(x, y))
        }
    }
}