package net.bncloud.excel.style.data;


public class ComplexHeadStyles  {

    /**
     *   表头横坐标 - 行
     * */
    private Integer x;
    /**
     *   表头纵坐标 - 列
     * */
    private Integer y;
    /**
     *   内置颜色
     * */
    private Short indexColor;

    public ComplexHeadStyles(Integer x, Integer y, Short indexColor){
        this.x=x;
        this.y=y;
        this.indexColor=indexColor;
    }

    private void setCroods(Integer x,Integer y){
        this.x=x;
        this.y=y;
    }



    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }

    public Short getIndexColor() {
        return indexColor;
    }

    public void setIndexColor(Short indexColor) {
        this.indexColor = indexColor;
    }
}
