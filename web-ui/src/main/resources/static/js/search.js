$(document).ready(searchForOffers());

function searchForOffers() {
    findCategories();
    $("#search-btn").click(findOffers);
}

function findCategories() {
    var settings = {
        url: "http://localhost:8765/manager-service/api/v1/categories",
        type: "GET",
        dataType: "json",
        success: function (data) {
            $(data).each(function (k, v) {
                $("#categories-list").append(createCategory(v));
            })
        }
    };
    $.ajax(settings);
}

function createCategory(category) {
    var categoryHTML =
        '<option id="category' + category.id + '">' + category.name + '</option>';
    return categoryHTML;
}

function findOffers() {
    var container = '<div class="container">';
    var filter = createOfferFilter();
    var settings = {
        url: "http://localhost:8765/catalog-service/api/v1/offers/filteredOffers",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(filter),
        dataType: "json",
        success: function (offers) {
            $(offers).each(function (k, v) {
                console.log(createOffer(v));
                /*$("#offer-area").append*/
                container += createOffer(v);
            });
            container += '</div>';
            var offerArea = $("#offer-area");
            offerArea.empty();
            offerArea.append(container);
        },
        error: function (error) {
            console.log(error);
        }
    };
    var promise = $.ajax(settings);
}

function createOfferFilter() {
    var categories = [];
    var tags = [];
    var price = [];
    $("#categories-list").find("option:selected").each(function (k, v) {
        categories.push(v.getAttribute("id").substr(8));
    });
    var tempTags = $("#tags-area").val().split(',');
    $(tempTags).each(function (k, v) {
        var tag = v.trim();
        if (tag.length !== 0) {
            tags.push(tag);
        }
    });
    var from = $("#from-price-field").val();
    var to = $("#to-price-field").val();
    if (from.length === 0) {
        from = "0";
    }
    if (to.length === 0) {
        to = "0";
    }
    price.push(from);
    price.push(to);
    var filter = {};
    if (tags.length !== 0) {
        filter["tags"] = tags;
    }
    if (categories.length !== 0) {
        filter["categories"] = categories;
    }
    filter["price"] = price;
    return filter;
}

function createOffer(offer) {
    var offerHTML;
    if (offer.available === false) {
        offerHTML =
            '<div class="offer unavailable" id="offer' + offer.id + '">' +
                '<div class="offer-name"><p>' + offer.name + '</p></div>' +
                '<div class="offer-category"><p>' + offer.category.name + '</p></div>' +
                '<div class="offer-description"><p>' + offer.description + '</p></div>' +
                '<div class="offer-price"><p>' + offer.price.value + ' $</p></div>' +
            '</div>'
    } else {
        offerHTML =
            '<div class="offer" id="offer' + offer.id + '">' +
                '<div class="offer-name"><p>' + offer.name + '</p></div>' +
                '<div class="offer-category"><p>' + offer.category.name + '</p></div>' +
                '<div class="offer-description"><p>' + offer.description + '</p></div>' +
                '<div class="offer-price"><p>' + offer.price.value + ' $</p></div>' +
            '</div>';
    }
    return offerHTML;
}