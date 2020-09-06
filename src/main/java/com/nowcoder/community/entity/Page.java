package com.nowcoder.community.entity;


//封装分页相关信息


public class Page {

    //页面传入数据
    //接受页面传入信息,当前页码
    private int current = 1;

    //每页上限
    private int limit = 10;


    //返回给页面的数据
    //数据总shu（用于计算总页数
    private int rows;

    //查询路径（用来复用分页链接）
    private String path;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        if(current>=1){

            this.current = current;
        }
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        if (limit >= 1 && limit <= 100){

            this.limit = limit;
        }
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        if (rows >= 0) {
            this.rows = rows;
        }
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    /**
     * 当前页得起始行
     */
    public int getOffset(){
        //( current - 1 ) * limit
        return (current-1) * limit;
    }
    /**
     * 总行数
     */
    public int getTotal(){
        if (rows % limit == 0) {
            return  rows / limit;
        }
        else{
            return rows / limit+1;
        }
    }

    /**
     * 起始页码
     */
    public int getFrom(){
        int from =current-2;
        return from<1?1:from;
    }
    /**
     * 结束页码
     */
    public  int getTo(){
        int to=current+2;
        return to>getTotal()?getTotal():to;
    }

}
