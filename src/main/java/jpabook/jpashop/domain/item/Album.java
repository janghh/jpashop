package jpabook.jpashop.domain.item;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Album extends Item {

    private String artist;
    private String etc;

    public Album(String artist, String etc, int price, int stockQuantity) {
        this.artist = artist;
        this.etc = etc;
        this.setPrice(price);
        this.setStockQuantity(stockQuantity);
    }



}
