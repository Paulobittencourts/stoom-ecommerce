package com.br.stoom.commerce.dto.resquest;

public class StockRequest {

    private Long productID;

    private Integer stock;

    public Long getProductID() {
        return productID;
    }

    public void setProductID(Long productID) {
        this.productID = productID;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}
