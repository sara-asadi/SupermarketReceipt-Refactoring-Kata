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
                Offer offer = this.offers.get(p);
                double unitPrice = this.catalog.getUnitPrice(p);
                int quantityAsInt = (int) quantity;
                Discount discount = null;
                int x = 1;
                if (offer.offerType == SpecialOfferType.ThreeForTwo) {
                    x = 3;

                } else if (offer.offerType == SpecialOfferType.TwoForAmount) {
                    x = 2;
                    if (quantityAsInt >= 2) {
                        int intDivision = quantityAsInt / x;
                        double pricePerUnit = offer.argument * intDivision;
                        double theTotal = (quantityAsInt % 2) * unitPrice;
                        double total = pricePerUnit + theTotal;
                        double discountN = unitPrice * quantity - total;
                        discount = new Discount(p, "2 for " + offer.argument, -discountN);
                    }

                } if (offer.offerType == SpecialOfferType.FiveForAmount) {
                    x = 5;
                }
                int numberOfXs = quantityAsInt / x;
                if (offer.offerType == SpecialOfferType.ThreeForTwo && quantityAsInt > 2) {
                    double discountAmount = quantity * unitPrice - ((numberOfXs * 2 * unitPrice) + quantityAsInt % 3 * unitPrice);
                    discount = new Discount(p, "3 for 2", -discountAmount);
                }
                if (offer.offerType == SpecialOfferType.TenPercentDiscount) {
                    discount = new Discount(p, offer.argument + "% off", -quantity * unitPrice * offer.argument / 100.0);
                }
                if (offer.offerType == SpecialOfferType.FiveForAmount && quantityAsInt >= 5) {
                    double discountTotal = unitPrice * quantity - (offer.argument * numberOfXs + quantityAsInt % 5 * unitPrice);
                    discount = new Discount(p, x + " for " + offer.argument, -discountTotal);
                }
                if (discount != null)
                    this.receipt.addDiscount(discount);
            }

        }
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
