function findCategories() {
    var settings = {
        url: "http://localhost:8765/catalog-service/api/v1/categories",
        type: "GET",
        dataType: "json",
        success: function (data) {
            $(data).each(function (k, v) {
                $("#category-container").append(createCategory(v));
            })
        }
    };
    $.ajax(settings);
}

function createCategory(category) {
    var categoryHTML =
        '<div class="category" id="category' + category.id + '">' +
            '<div class="category-name"><p>' + category.name + '</p></div> ';
    var offerAreaHTML =
        '<div class="offer-area">';
    var filter = {"categories": [category.id]};
    var settings = {
        url: "http://localhost:8765/catalog-service/api/v1/offers/filteredOffers",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify(filter),
        dataType: "json",
        async: false,
        success: function (offers) {
            $(offers).each(function (k, v) {
                offerAreaHTML += createOffer(v);
            });
            offerAreaHTML += '</div>'
        }
    };
    var promise = $.ajax(settings);
    promise.done(function () {
        categoryHTML += offerAreaHTML;
        categoryHTML += '</div>';
    });
    return categoryHTML;
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

findCategories();