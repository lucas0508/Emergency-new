package com.tjmedicine.emergency.common.net;

import java.util.List;


public class ResponseDataEntity<T> {


    //-----------------------[ 必传项 ]-----------------------//

    private Integer code;               // 状态码
    private String msg;                 // 响应信息
    private Long timestamp;             // 当前服务器时间


    //-----------------------[ page ]-----------------------//

    private List<T> rows;               // 列表
    private Integer total;              // 总数


    //-----------------------[ page/list ]-----------------------//

    private Integer current;            // 当前页
    private Integer size;               // 每页数量
    private Integer pages;              // 总页数


    //-----------------------[ other ]-----------------------//

    private List<T> data;


    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public Integer getCurrent() {
        return current;
    }

    public void setCurrent(Integer current) {
        this.current = current;
    }

    public Integer getSize() {
        return size;
    }


    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public List<T> getData() {
        return data;
    }


    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseEntity{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", timestamp=" + timestamp +
                ", rows=" + rows +
                ", total=" + total +
                ", current=" + current +
                ", size=" + size +
                ", pages=" + pages +
                ", data=" + data +
                '}';
    }
}
