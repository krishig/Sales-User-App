package com.krishig.android.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Order {
    @SerializedName("content")
    @Expose
    public ArrayList<Content> content;
    @SerializedName("pageNumber")
    @Expose
    public String pageNumber;
    @SerializedName("pageSize")
    @Expose
    public String pageSize;
    @SerializedName("totalElements")
    @Expose
    public String totalElements;
    @SerializedName("totalPages")
    @Expose
    public int totalPages;
    @SerializedName("lastPage")
    @Expose
    public Boolean lastPage;

    public ArrayList<Content> getContent() {
        return content;
    }

    public void setContent(ArrayList<Content> content) {
        this.content = content;
    }

    public String getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(String pageNumber) {
        this.pageNumber = pageNumber;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public String getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(String totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public Boolean getLastPage() {
        return lastPage;
    }

    public void setLastPage(Boolean lastPage) {
        this.lastPage = lastPage;
    }

    public class Content {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("orderId")
        @Expose
        public String orderId;
        @SerializedName("customerId")
        @Expose
        public CustomerId customerId;
        @SerializedName("totalPrice")
        @Expose
        public String totalPrice;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("paymentMethod")
        @Expose
        public String paymentMethod;
        @SerializedName("contactNumber")
        @Expose
        public String contactNumber;
        @SerializedName("addressId")
        @Expose
        public String addressId;
        @SerializedName("createdBy")
        @Expose
        public String createdBy;
        @SerializedName("createdDate")
        @Expose
        public String createdDate;
        @SerializedName("modifiedDate")
        @Expose
        public String modifiedDate;
        @SerializedName("modifiedBy")
        @Expose
        public String modifiedBy;
        @SerializedName("closedDate")
        @Expose
        public String closedDate;
        @SerializedName("productResponseDtos")
        @Expose
        public ArrayList<ProductResponseDtos> productResponseDtos;

        public ArrayList<ProductResponseDtos> getProductResponseDtos() {
            return productResponseDtos;
        }

        public void setProductResponseDtos(ArrayList<ProductResponseDtos> productResponseDtos) {
            this.productResponseDtos = productResponseDtos;
        }

        public class CustomerId {

            @SerializedName("id")
            @Expose
            public String id;
            @SerializedName("fullName")
            @Expose
            public String fullName;
            @SerializedName("mobileNumber")
            @Expose
            public String mobileNumber;
            @SerializedName("gender")
            @Expose
            public String gender;
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

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getFullName() {
                return fullName;
            }

            public void setFullName(String fullName) {
                this.fullName = fullName;
            }

            public String getMobileNumber() {
                return mobileNumber;
            }

            public void setMobileNumber(String mobileNumber) {
                this.mobileNumber = mobileNumber;
            }

            public String getGender() {
                return gender;
            }

            public void setGender(String gender) {
                this.gender = gender;
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


        public class ProductResponseDtos {
            @SerializedName("productImageResponse")
            @Expose
            public ProductImageResponse productImageResponse;

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

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public CustomerId getCustomerId() {
            return customerId;
        }

        public void setCustomerId(CustomerId customerId) {
            this.customerId = customerId;
        }

        public String getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(String totalPrice) {
            this.totalPrice = totalPrice;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public void setPaymentMethod(String paymentMethod) {
            this.paymentMethod = paymentMethod;
        }

        public String getContactNumber() {
            return contactNumber;
        }

        public void setContactNumber(String contactNumber) {
            this.contactNumber = contactNumber;
        }

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
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

        public String getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public String getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(String modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public String getClosedDate() {
            return closedDate;
        }

        public void setClosedDate(String closedDate) {
            this.closedDate = closedDate;
        }
    }


    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("orderId")
    @Expose
    public String orderId;
    @SerializedName("customerId")
    @Expose
    public Content.CustomerId customerId;
    @SerializedName("totalPrice")
    @Expose
    public String totalPrice;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("paymentMethod")
    @Expose
    public String paymentMethod;
    @SerializedName("contactNumber")
    @Expose
    public String contactNumber;
    @SerializedName("addressId")
    @Expose
    public String addressId;
    @SerializedName("createdBy")
    @Expose
    public String createdBy;
    @SerializedName("createdDate")
    @Expose
    public String createdDate;
    @SerializedName("modifiedDate")
    @Expose
    public String modifiedDate;
    @SerializedName("modifiedBy")
    @Expose
    public String modifiedBy;
    @SerializedName("closedDate")
    @Expose
    public String closedDate;
    @SerializedName("productResponseDtos")
    @Expose
    public ArrayList<ProductResponseDtos> productResponseDtos;
    @SerializedName("addressResponseDto")
    @Expose
    public AddressResponseDto addressResponseDto;

    public AddressResponseDto getAddressResponseDto() {
        return addressResponseDto;
    }

    public void setAddressResponseDto(AddressResponseDto addressResponseDto) {
        this.addressResponseDto = addressResponseDto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Content.CustomerId getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Content.CustomerId customerId) {
        this.customerId = customerId;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
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

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getClosedDate() {
        return closedDate;
    }

    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate;
    }

    public ArrayList<ProductResponseDtos> getProductResponseDtos() {
        return productResponseDtos;
    }

    public void setProductResponseDtos(ArrayList<ProductResponseDtos> productResponseDtos) {
        this.productResponseDtos = productResponseDtos;
    }

    public class ProductResponseDtos {
        @SerializedName("productName")
        @Expose
        public String productName;
        @SerializedName("quantity")
        @Expose
        public String quantity;
        @SerializedName("discount")
        @Expose
        public String discount;
        @SerializedName("price")
        @Expose
        public String price;
        @SerializedName("discountPrice")
        @Expose
        public String discountPrice;

        @SerializedName("productImageResponse")
        @Expose
        public ProductImageResponse productImageResponse;

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getDiscountPrice() {
            return discountPrice;
        }

        public void setDiscountPrice(String discountPrice) {
            this.discountPrice = discountPrice;
        }

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

        public ProductImageResponse getProductImageResponse() {
            return productImageResponse;
        }

        public void setProductImageResponse(ProductImageResponse productImageResponse) {
            this.productImageResponse = productImageResponse;
        }
    }

    public class AddressResponseDto {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("customer")
        @Expose
        public String customer;
        @SerializedName("houseNumber")
        @Expose
        public String houseNumber;
        @SerializedName("streetName")
        @Expose
        public String streetName;
        @SerializedName("villageName")
        @Expose
        public String villageName;
        @SerializedName("district")
        @Expose
        public String district;
        @SerializedName("state")
        @Expose
        public String state;
        @SerializedName("postalCode")
        @Expose
        public String postalCode;
        @SerializedName("createdBy")
        @Expose
        public String createdBy;
        @SerializedName("createdDate")
        @Expose
        public String createdDate;
        @SerializedName("modifiedBy")
        @Expose
        public Object modifiedBy;
        @SerializedName("modifiedDate")
        @Expose
        public String modifiedDate;
        @SerializedName("customerCartResponseDto")
        @Expose
        public Object customerCartResponseDto;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getCustomer() {
            return customer;
        }

        public void setCustomer(String customer) {
            this.customer = customer;
        }

        public String getHouseNumber() {
            return houseNumber;
        }

        public void setHouseNumber(String houseNumber) {
            this.houseNumber = houseNumber;
        }

        public String getStreetName() {
            return streetName;
        }

        public void setStreetName(String streetName) {
            this.streetName = streetName;
        }

        public String getVillageName() {
            return villageName;
        }

        public void setVillageName(String villageName) {
            this.villageName = villageName;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getPostalCode() {
            return postalCode;
        }

        public void setPostalCode(String postalCode) {
            this.postalCode = postalCode;
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

        public Object getModifiedBy() {
            return modifiedBy;
        }

        public void setModifiedBy(Object modifiedBy) {
            this.modifiedBy = modifiedBy;
        }

        public String getModifiedDate() {
            return modifiedDate;
        }

        public void setModifiedDate(String modifiedDate) {
            this.modifiedDate = modifiedDate;
        }

        public Object getCustomerCartResponseDto() {
            return customerCartResponseDto;
        }

        public void setCustomerCartResponseDto(Object customerCartResponseDto) {
            this.customerCartResponseDto = customerCartResponseDto;
        }

    }

}
    
