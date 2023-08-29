package dev.authorises.cavelet.customitems;

public enum Rarity {
    COMMON("COMMON", "<#79918c>", "<#364e59>Common"),
    UNCOMMON("UNCOMMON", "<#30cccf>", "<#30cccf>Uncommon"),
    RARE("RARE", "<#4de85f>", "<#4de85f>Rare"),
    DIVINE("DIVINE", "<#db65d7>", "<#db65d7>Divine"),
    SINGULAR("SINGULAR", "<#d43f60>", "<gradient:#d43f60:#d43fb6>Singular</gradient>");

    public String color;
    public String displayName;
    public String id;

    Rarity(String id, String color, String displayName){
        this.id = id;
        this.color = color;
        this.displayName = displayName;
    }
}
