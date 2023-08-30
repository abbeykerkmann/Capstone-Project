package seg4910.group17.capstoneserver.api.model.resource;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ListingFilterResource {

    @JsonProperty
    private List<String> keywords;

    @JsonProperty
    private Double priceLow;

    @JsonProperty
    private Double priceHigh;

    @JsonProperty
    private String location;

    public ListingFilterResource(List<String> keywords, Double priceLow, Double priceHigh, String location) {
        this.keywords = keywords;
        this.priceLow = priceLow;
        this.priceHigh = priceHigh;
        this.location = location;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public Double getPriceLow() {
        return priceLow;
    }

    public Double getPriceHigh() {
        return priceHigh;
    }

    public String getLocation() {
        return location;
    }
}
