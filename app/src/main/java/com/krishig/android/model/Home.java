package com.krishig.android.model;

public class Home {
    String orderIdDetailTextView;
    String customerNameDetailTextView;
    String productDetailTextView;
    String quantityDetailTextView;
    String totalAmountDetailTextView;

    public String getOrderIdDetailTextView() {
        return orderIdDetailTextView;
    }

    public void setOrderIdDetailTextView(String orderIdDetailTextView) {
        this.orderIdDetailTextView = orderIdDetailTextView;
    }

    public String getCustomerNameDetailTextView() {
        return customerNameDetailTextView;
    }

    public void setCustomerNameDetailTextView(String customerNameDetailTextView) {
        this.customerNameDetailTextView = customerNameDetailTextView;
    }

    public String getProductDetailTextView() {
        return productDetailTextView;
    }

    public void setProductDetailTextView(String productDetailTextView) {
        this.productDetailTextView = productDetailTextView;
    }

    public String getQuantityDetailTextView() {
        return quantityDetailTextView;
    }

    public void setQuantityDetailTextView(String quantityDetailTextView) {
        this.quantityDetailTextView = quantityDetailTextView;
    }

    public String getTotalAmountDetailTextView() {
        return totalAmountDetailTextView;
    }

    public void setTotalAmountDetailTextView(String totalAmountDetailTextView) {
        this.totalAmountDetailTextView = totalAmountDetailTextView;
    }

    public Home(String orderIdDetailTextView, String customerNameDetailTextView, String productDetailTextView, String quantityDetailTextView, String totalAmountDetailTextView) {
        this.orderIdDetailTextView = orderIdDetailTextView;
        this.customerNameDetailTextView = customerNameDetailTextView;
        this.productDetailTextView = productDetailTextView;
        this.quantityDetailTextView = quantityDetailTextView;
        this.totalAmountDetailTextView = totalAmountDetailTextView;
    }
}
