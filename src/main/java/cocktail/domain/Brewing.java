package cocktail.domain;

public enum Brewing {
    BLENDING("블렌딩"),
    BUILDING("빌딩"),
    SHAKING("쉐이킹"),
    STIRRING("스터링"),
    FLOATING("플로팅"),
    THROWING("쓰로잉");

    private final String korean;

    Brewing(String korean) {
        this.korean = korean;
    }
}
