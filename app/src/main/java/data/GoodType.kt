package data

data class Position(var x: Int, var y: Int)

data class GoodsTypePosition(val row: Int, val col: Int)

data class GoodType(val name: String, val position: Position, val goodsType: GoodsTypePosition)
{
    class Build
    {
        var x = 0
        var y = 0
        var row = 0
        var col = 0
        var name: String = ""

        fun build(): GoodType
        {
            return GoodType(name, Position(x, y), GoodsTypePosition(row, col))
        }
    }
}