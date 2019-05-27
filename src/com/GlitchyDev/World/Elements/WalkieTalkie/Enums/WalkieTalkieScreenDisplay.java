package com.GlitchyDev.World.Elements.WalkieTalkie.Enums;

import java.util.ArrayList;

public enum WalkieTalkieScreenDisplay {
    // Menu Items
    BATTERY_4(0,0),
    BATTERY_3(0,1),
    BATTERY_2(0,2),
    BATTERY_1(0,3),
    BATTERY_0(0,4),

    VOLUME_3(1,0),
    VOLUME_2(1,1),
    VOLUME_1(1,2),
    VOLUME_0(1,3),
    VOLUME_MUTE(1,4),
    VOLUME_ADD(1,5),
    VOLUME_SUB(1,6),

    CHANNEL_0(2,0),
    CHANNEL_1(2,1),
    CHANNEL_2(2,2),
    CHANNEL_3(2,3),
    CHANNEL_4(2,4),
    CHANNEL_5(2,5),
    CHANNEL_6(2,6),
    CHANNEL_7(2,7),
    CHANNEL_8(3,0),
    CHANNEL_9(3,1),
    CHANNEL_UNKNOWN(3,2),

    // Callsigns
    CALLSIGN_UNITY(4,0),
    CALLSIGN_BRIDGE(4,1),
    CALLSIGN_CROSS(4,2),
    CALLSIGN_OUROBOROS(4,3),
    CALLSIGN_CURSOR(4,4),
    CALLSIGN_TWIST(4,5),
    CALLSIGN_CLAW(4,6),
    CALLSIGN_ILLUSION(4,7),

    CALLSIGN_NAILS(5,0),
    CALLSIGN_ANTLERS(5,1),
    CALLSIGN_TWINS_A(5,2),
    CALLSIGN_TWINS_1(5,3),
    CALLSIGN_MIRROR(5,4),
    CALLSIGN_EYE(5,5),
    CALLSIGN_REFLECTION(5,6),
    CALLSIGN_CROWN(5,7),

    CALLSIGN_WINGS(6,0),
    CALLSIGN_VASE(6,1),
    CALLSIGN_NEGATE(6,2),
    CALLSIGN_TEMPLE(6,3),
    CALLSIGN_CROSSHAIR(6,4),
    CALLSIGN_NEEDLE(6,5),
    CALLSIGN_DISTORTION(6,6),
    CALLSIGN_FLIGHT(6,7),

    CALLSIGN_EAGLE(7,0),
    CALLSIGN_ANCHOR(7,1),
    CALLSIGN_STAIRS(7,2),
    CALLSIGN_SWING(7,3),
    CALLSIGN_SIGNAL(7,4),
    CALLSIGN_CONNECTION(7,5),
    CALLSIGN_ALPHA(7,6),
    CALLSIGN_KARMA(7,7),

    // Message Symbols
    A(8,0),
    B(8,1),
    C(8,2),
    D(8,3),
    E(8,4),
    F(8,5),
    G(8,6),
    H(8,7),

    I(9,0),
    J(9,1),
    K(9,2),
    L(9,3),
    M(9,4),
    N(9,5),
    O(9,6),
    P(9,7),

    Q(10,0),
    R(10,1),
    S(10,2),
    T(10,3),
    U(10,4),
    V(10,5),
    W(10,6),
    X(10,7),

    Y(11,0),
    Z(11,1),
    ZERO(11,2),
    ONE(11,3),
    TWO(11,4),
    THREE(11,5),
    FOUR(11,6),
    FIVE(11,7),

    SIX(12,0),
    SEVEN(12,1),
    EIGHT(12,2),
    NINE(12,3),
    PERIOD(12,4),
    COMMA(12,5),
    EXCLAMATION_MARK(12,6),
    QUESTION_MARK(12,7),

    UNDERLINE(13,0),
    PERCENT_SIGN(13,1),
    SPACE(13,2),


    // Inhabitant
    INHABITANT_NEUTRAL(14,0),
    INHABITANT_CRY_1(14,1),
    INHABITANT_CRY_2(14,2),
    INHABITANT_CRY_3(14,3),
    INHABITANT_CRY_4(14,4),
    INHABITANT_CRY_5(14,5),

    INHABITANT_SERIOUSLY_1(15,0),
    INHABITANT_SERIOUS_1(15,1),
    INHABITANT_WTF_1(15,2),
    INHABITANT_LEFT_1(15,3),
    INHABITANT_RIGHT_1(15,4),
    INHABITANT_UP_1(15,5),
    INHABITANT_DOWN_1(15,6),
    INHABITANT_EYE_CLOSED_1(15,7),
    INHABITANT_BLUSH_LEFT_1(15,8),
    INHABITANT_BLUSH_RIGHT_1(15,9),

    INHABITANT_SERIOUSLY_2(16,0),
    INHABITANT_SERIOUS_2(16,1),
    INHABITANT_WTF_2(16,2),
    INHABITANT_LEFT_2(16,3),
    INHABITANT_RIGHT_2(16,4),
    INHABITANT_UP_2(16,5),
    INHABITANT_DOWN_2(16,6),
    INHABITANT_EYE_CLOSED_2(16,7),
    INHABITANT_BLUSH_LEFT_2(16,8),
    INHABITANT_BLUSH_RIGHT_2(16,9),

    INHABITANT_SERIOUSLY_3(17,0),
    INHABITANT_SERIOUS_3(17,1),
    INHABITANT_WTF_3(17,2),
    INHABITANT_LEFT_3(17,3),
    INHABITANT_RIGHT_3(17,4),
    INHABITANT_UP_3(17,5),
    INHABITANT_DOWN_3(17,6),
    INHABITANT_EYE_CLOSED_3(17,7),
    INHABITANT_BLUSH_LEFT_3(17,8),
    INHABITANT_BLUSH_RIGHT_3(17,9),

    INHABITANT_DRAWING_HAPPY(17,0),
    INHABITANT_DRAWING_SAD(17,1),
    INHABITANT_DRAWING_UMM(17,2),
    INHABITANT_DRAWING_SERIOUSLY(17,3),
    INHABITANT_DRAWING_BLUSH(17,4),
    INHABITANT_DRAWING_ALRIGHT(17,5),
    ;

    public static WalkieTalkieScreenDisplay getLetter(char letter) {
        switch(letter) {
            case 'A':
                return A;
            case 'B':
                return B;
            case 'C':
                return C;
            case 'D':
                return D;
            case 'E':
                return E;
            case 'F':
                return F;
            case 'G':
                return G;
            case 'H':
                return H;
            case 'I':
                return I;
            case 'J':
                return J;
            case 'K':
                return K;
            case 'L':
                return L;
            case 'M':
                return M;
            case 'N':
                return N;
            case 'O':
                return O;
            case 'P':
                return P;
            case 'Q':
                return Q;
            case 'R':
                return R;
            case 'S':
                return S;
            case 'T':
                return I;
            case 'U':
                return U;
            case 'V':
                return V;
            case 'W':
                return W;
            case 'X':
                return X;
            case 'Y':
                return Y;
            case 'Z':
                return Z;
            case '0':
                return ZERO;
            case '1':
                return ONE;
            case '2':
                return TWO;
            case '3':
                return THREE;
            case '4':
                return FOUR;
            case '5':
                return FIVE;
            case '6':
                return SIX;
            case '7':
                return SEVEN;
            case '8':
                return EIGHT;
            case '9':
                return NINE;
            case '.':
                return PERIOD;
            case ',':
                return COMMA;
            case '!':
                return EXCLAMATION_MARK;
            case '?':
                return QUESTION_MARK;
            case '_':
                return UNDERLINE;
            case '%':
                return PERCENT_SIGN;
            case ' ':
                return SPACE;
        }
        return SPACE;
    }

    public ArrayList<WalkieTalkieScreenDisplay> getScreenDisplays(String line) {
        ArrayList<WalkieTalkieScreenDisplay> list = new ArrayList<>(line.length());
        for(char c: line.toUpperCase().toCharArray()) {
            list.add(getLetter(c));
        }
        return list;
    }

    public static WalkieTalkieScreenDisplay getVolumeDisplay(int volumeLevel) {
        switch(volumeLevel) {
            case 3:
                return WalkieTalkieScreenDisplay.VOLUME_3;
            case 2:
                return WalkieTalkieScreenDisplay.VOLUME_2;
            case 1:
                return WalkieTalkieScreenDisplay.VOLUME_1;
            case 0:
                return WalkieTalkieScreenDisplay.VOLUME_0;
            default:
                return WalkieTalkieScreenDisplay.VOLUME_0;
        }
    }

    public static WalkieTalkieScreenDisplay getBatteryDisplay(int batteryLevel) {
        switch(batteryLevel) {
            case 4:
                return WalkieTalkieScreenDisplay.BATTERY_4;
            case 3:
                return WalkieTalkieScreenDisplay.BATTERY_3;
            case 2:
                return WalkieTalkieScreenDisplay.BATTERY_2;
            case 1:
                return WalkieTalkieScreenDisplay.BATTERY_1;
            case 0:
                return WalkieTalkieScreenDisplay.BATTERY_0;
            default:
                return WalkieTalkieScreenDisplay.BATTERY_0;
        }
    }

    public static WalkieTalkieScreenDisplay getChannelDisplay(int channel) {
        switch(channel) {
            case 0:
                return CHANNEL_0;
            case 1:
                return CHANNEL_1;
            case 2:
                return CHANNEL_2;
            case 3:
                return CHANNEL_3;
            case 4:
                return CHANNEL_4;
            case 5:
                return CHANNEL_5;
            case 6:
                return CHANNEL_6;
            case 7:
                return CHANNEL_7;
            case 8:
                return CHANNEL_8;
            case 9:
                return CHANNEL_9;
            default:
                return CHANNEL_UNKNOWN;
        }
    }

    private final int x;
    private final int y;
    WalkieTalkieScreenDisplay(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
