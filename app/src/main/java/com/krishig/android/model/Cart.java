package com.krishig.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Cart {
    @SerializedName("cartProductResponseDtoList")
    @Expose
    public ArrayList<CartProductResponseDto> cartProductResponseDtoList;
    @SerializedName("totalPrice")
    @Expose
    public String totalPrice;
    @SerializedName("orderId")
    @Expose
    public String orderId;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public ArrayList<CartProductResponseDto> getCartProductResponseDtoList() {
        return cartProductResponseDtoList;
    }

    public void setCartProductResponseDtoList(ArrayList<CartProductResponseDto> cartProductResponseDtoList) {
        this.cartProductResponseDtoList = cartProductResponseDtoList;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public class CartProductResponseDto {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("cartId")
        @Expose
        public String cartId;
        @SerializedName("productResponseDto")
        @Expose
        public ProductResponseDto productResponseDto;
        @SerializedName("productQuantity")
        @Expose
        public String productQuantity;
        @SerializedName("discountPrice")
        @Expose
        public String discountPrice;
        @SerializedName("discount")
        @Expose
        public String discount;
        @SerializedName("totalProductDiscountPrice")
        @Expose
        public String totalProductDiscountPrice;
        @SerializedName("createdBy")
        @Expose
        public String createdBy;
        @SerializedName("createdDate")
        @Expose
        public String createdDate;
        @SerializedName("modifiedBy")
        @Expose
        public String modifiedBy;
        @SerializedName("modifiedDate")
        @Expose
        public String modifiedDate;

        public class ProductImageResponse {

            @SerializedName("imageName")
            @Expose
            public String imageName;
            @SerializedName("imageUrl")
            @Expose
            public String imageUrl;

            public String getImageName() {
                return imageName;
            }

            public void setImageName(String imageName) {
                this.imageName = imageName;
            }

            public String getImageUrl() {
                return imageUrl;
            }

            public void setImageUrl(String imageUrl) {
                this.imageUrl = imageUrl;
            }
        }

        public class ProductResponseDto {

            @SerializedName("id")
            @Expose
            public String id;
            @SerializedName("productName")
            @Expose
            public String productName;
            @SerializedName("subCategory")
            @Expose
            public String subCategory;
            @SerializedName("brandId")
            @Expose
            public String brandId;
            @SerializedName("productDescription")
            @Expose
            public String productDescription;
            @SerializedName("price")
            @Expose
            public String price;
            @SerializedName("discount")
            @Expose
            public String discount;
            @SerializedName("productImageResponse")
            @Expose
            public ProductImageResponse productImageResponse;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
            }

            public String getSubCategory() {
                return subCategory;
            }

            public void setSubCategory(String subCategory) {
                this.subCategory = subCategory;
            }

            public String getBrandId() {
                return brandId;
            }

            public void setBrandId(String brandId) {
                this.brandId = brandId;
            }

            public String getProductDescription() {
                return productDescription;
            }

            public void setProductDescription(String productDescription) {
                this.productDescription = productDescription;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getDiscount() {
                return discount;
            }

            public void setDiscount(String discount) {
                this.discount = discount;
            }

            public ProductImageResponse getProductImageResponse() {
                return productImageResponse;
            }

            public void setProductImageResponse(ProductImageResponse productImageResponse) {
                this.productImageResponse = productImageResponse;
            }
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCartId() {
            return cartId;
        }

        public void setCartId(String cartId) {
            this.cartId = cartId;
        }

        public ProductResponseDto getProductResponseDto() {
            return productResponseDto;
        }

        public void setProductResponseDto(ProductResponseDto productResponseDto) {
            this.productResponseDto = productResponseDto;
        }

        public String getProductQuantity() {
            return productQuantity;
        }

        public void setProductQuantity(String productQuantity) {
            this.productQuantity = productQuantity;
        }

        public String getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(String discountPrice) {
            this.discountPrice = discountPrice;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getTotalProductDiscountPrice() {
            return totalProductDiscountPrice;
        }

        public void setTotalProductDiscountPrice(String totalProductDiscountPrice) {
            this.totalProductDiscountPrice = totalProductDiscountPrice;
        }

        public String getCreatedBy() {
            return createdBy;
        }

        public void setCreatedBy(String createdBy) {
            this.createdBy = createdBy;
        }

        public String getCreatedDate() {
            return createdDate;
        }

        public void setCreatedDate(String createdDate) {
            this.createdDate = createdDate;
        }

        public String getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public String getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
        }
    }
}
