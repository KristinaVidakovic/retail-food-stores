package com.retail_food_stores.ws.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EstablishmentType {

    A("A"),
    JABC("JABC"),
    JAC("JAC"),
    JABCH("JABCH"),
    JACH("JACH"),
    JAW("JAW"),
    JABH("JABH"),
    JACDH("JACDH"),
    JABCD("JABCD"),
    JACW("JACW"),
    JACK("JACK"),
    JACD("JACD"),
    JACHK("JACHK"),
    JAD("JAD"),
    JACDK("JACDK"),
    JAB("JAB"),
    JABHK("JABHK"),
    JABCO("JABCO"),
    JABCHK("JABCHK"),
    JABCK("JABCK"),
    JADHK("JADHK"),
    JABCG("JABCG"),
    JABCOP("JABCOP"),
    JACDHK("JACDHK"),
    JKA("JKA"),
    JACX("JACX"),
    JADI("JADI"),
    JAN("JAN"),
    JACE("JACE"),
    JKDA("JKDA"),
    JACO("JACO"),
    JADK("JADK"),
    JAO("JAO"),
    JACG("JACG"),
    JACDKM("JACDKM"),
    JABCHO("JABCHO"),
    JABCR("JABCR"),
    JAE("JAE"),
    JACN("JACN"),
    JACV("JACV"),
    JACIK("JACIK"),
    JAS("JAS"),
    JACP("JACP"),
    JACU("JACU"),
    JABCS("JABCS"),
    JACDEW("JACDEW"),
    JAK("JAK"),
    JABCP("JABCP"),
    JAV("JAV"),
    JABK("JABK"),
    JACZ("JACZ"),
    JAM("JAM"),
    JACEH("JACEH"),
    JABCDH("JABCDH"),
    JACDG("JACDG"),
    JABCDK("JABCDK"),
    JABCW("JABCW"),
    JACDGH("JACDGH"),
    JACEW("JACEW"),
    JABDK("JABDK"),
    JACHOP("JACHOP"),
    JAZ("JAZ"),
    JACDE("JACDE"),
    JACS("JACS"),
    JADX("JADX"),
    JABCDP("JABCDP"),
    JACM("JACM"),
    JACI("JACI"),
    JACHN("JACHN"),
    JACHKO("JACHKO"),
    JAOP("JAOP"),
    JACEK("JACEK"),
    JACOP("JACOP"),
    JABCKO("JABCKO"),
    JADEH("JADEH"),
    JACR("JACR"),
    JADN("JADN"),
    JACDEK("JACDEK"),
    JABCGP("JABCGP"),
    JACQS("JACQS"),
    JACHO("JACHO"),
    JADE("JADE"),
    JAEHK("JAEHK");

    private final String value;

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    @JsonCreator
    public static EstablishmentType fromValue(String value){
        for (EstablishmentType m : EstablishmentType.values()) {
            if (m.value.equals(value)){
                return m;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }
}
