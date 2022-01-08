package dojo.supermarket.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Teller {

    private final SupermarketCatalog catalog;
    private Map<Product, Offer> offers = new HashMap<>();
    private Receipt receipt;
    public Teller(SupermarketCatalog catalog) {
        this.catalog = catalog;
    }

    public void addSpecialOffer(SpecialOfferType offerType, Product product, double argument) {
        this.offers.put(product, new Offer(offerType, product, argument));
    }

    public Receipt checksOutArticlesFrom(ShoppingCart theCart) {
        getReceipt(theCart);
        handleOffers(theCart.getProductQuantities());

        return this.receipt;
    }

    private void handleOffers(Map<Product, Double> productQuantities) {
        for (Product p: productQuantities.keySet()) {
            double quantity = productQuantities.get(p);
            if (this.offers.containsKey(p)) {
                offersHandlerPerItem(p, quantity);
            }
        }
    }

    private void offersHandlerPerItem(Product p, double quantity) {
        Offer offer = this.offers.get(p);
        double unitPrice = this.catalog.getUnitPrice(p);
        Discount discount = null;

        if (offer.offerType == SpecialOfferType.ThreeForTwo)
            discount = offer.handleThreeForTwo(p, quantity, unitPrice);
        else if (offer.offerType == SpecialOfferType.TwoForAmount)
            discount = offer.handleTwoForAmount(p, quantity, unitPrice);
        if (offer.offerType == SpecialOfferType.FiveForAmount)
            discount = offer.handleFiveForAmount(p, quantity, unitPrice);
        if (offer.offerType == SpecialOfferType.TenPercentDiscount)
            discount = offer.handleTenPercentDiscount(p, quantity, unitPrice);
        if (discount != null)
            this.receipt.addDiscount(discount);
    }

    private void getReceipt(ShoppingCart theCart) {
        this.receipt = new Receipt();
        List<ProductQuantity> productQuantities = theCart.getItems();
        for (ProductQuantity pq: productQuantities) {
            Product p = pq.getProduct();
            double quantity = pq.getQuantity();
            double unitPrice = this.catalog.getUnitPrice(p);
            double price = quantity * unitPrice;
            this.receipt.addProduct(p, quantity, unitPrice, price);
        }
    }

}
