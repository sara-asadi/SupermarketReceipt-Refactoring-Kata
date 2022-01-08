package dojo.supermarket.model;

public class Offer {
    SpecialOfferType offerType;
    private final Product product;
    double argument;

    public Offer(SpecialOfferType offerType, Product product, double argument) {
        this.offerType = offerType;
        this.argument = argument;
        this.product = product;
    }

    public Discount handleTenPercentDiscount(Product p, double quantity, double unitPrice) {
        Discount discount;
        discount = new Discount(p, this.argument + "% off", -quantity * unitPrice * this.argument / 100.0);
        return discount;
    }

    public Discount handleFiveForAmount(Product p, double quantity, double unitPrice) {
        int offerPackSize = 5;
        int numberOfPacks = ((int) quantity) / offerPackSize;
        if (((int) quantity) >= 5) {
            double discountTotal = unitPrice * quantity - (this.argument * numberOfPacks + ((int) quantity) % 5 * unitPrice);
            return new Discount(p, offerPackSize + " for " + this.argument, -discountTotal);
        }
        return null;
    }

    public Discount handleTwoForAmount(Product p, double quantity, double unitPrice) {
        int offerPackSize = 2;

        if (((int) quantity) >= 2) {
            int intDivision = ((int) quantity) / offerPackSize;
            double pricePerUnit = this.argument * intDivision;
            double theTotal = (((int) quantity) % 2) * unitPrice;
            double total = pricePerUnit + theTotal;
            double discountN = unitPrice * quantity - total;
            return new Discount(p, "2 for " + this.argument, -discountN);
        }
        return null;
    }

    public Discount handleThreeForTwo(Product p, double quantity, double unitPrice) {
        int offerPackSize = 3;
        int numberOfPacks = ((int) quantity) / offerPackSize;
        if (((int) quantity) > 2) {
            double discountAmount = quantity * unitPrice - ((numberOfPacks * 2 * unitPrice) + ((int) quantity) % 3 * unitPrice);
            return new Discount(p, "3 for 2", -discountAmount);
        }
        return null;
    }

}
