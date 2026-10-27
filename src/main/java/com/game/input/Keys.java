package com.game.input;

import com.game.Init;
import com.game.screens.LoadingScreen;

@Init(stage = 1)
public enum Keys {
  A("a", KeyType.LETTER, 65),
  B("b", KeyType.LETTER, 66),
  C("c", KeyType.LETTER, 67),
  D("d", KeyType.LETTER, 68),
  E("e", KeyType.LETTER, 69),
  F("f", KeyType.LETTER, 70),
  G("g", KeyType.LETTER, 71),
  H("h", KeyType.LETTER, 72),
  I("i", KeyType.LETTER, 73),
  J("j", KeyType.LETTER, 74),
  K("k", KeyType.LETTER, 75),
  L("l", KeyType.LETTER, 76),
  M("m", KeyType.LETTER, 77),
  N("n", KeyType.LETTER, 78),
  O("o", KeyType.LETTER, 79),
  P("p", KeyType.LETTER, 80),
  Q("q", KeyType.LETTER, 81),
  R("r", KeyType.LETTER, 82),
  S("s", KeyType.LETTER, 83),
  T("t", KeyType.LETTER, 84),
  U("u", KeyType.LETTER, 85),
  V("v", KeyType.LETTER, 86),
  W("w", KeyType.LETTER, 87),
  X("x", KeyType.LETTER, 88),
  Y("y", KeyType.LETTER, 89),
  Z("z", KeyType.LETTER, 90),
  SPACE("space", KeyType.LETTER, 32),
  
  GRAVE("`", KeyType.LETTER, 39),
  MINUS("-", KeyType.LETTER, 45),
  EQUALS("=", KeyType.LETTER, 61),
  LEFT_BRACKET("[", KeyType.LETTER, 91),
  RIGHT_BRACKET("]", KeyType.LETTER, 93),
  BACKSLASH("\\", KeyType.LETTER, 92),
  SEMICOLON(";", KeyType.LETTER, 59),
  APOSTROPHE("'", KeyType.LETTER, 39),
  COMMA(",", KeyType.LETTER, 44),
  PERIOD(".", KeyType.LETTER, 46),
  SLASH("/", KeyType.LETTER, 47),
  
  NUM0("0", KeyType.NUMBER, 48),
  NUM1("1", KeyType.NUMBER, 49),
  NUM2("2", KeyType.NUMBER, 50),
  NUM3("3", KeyType.NUMBER, 51),
  NUM4("4", KeyType.NUMBER, 52),
  NUM5("5", KeyType.NUMBER, 53),
  NUM6("6", KeyType.NUMBER, 54),
  NUM7("7", KeyType.NUMBER, 55),
  NUM8("8", KeyType.NUMBER, 56),
  NUM9("9", KeyType.NUMBER, 57),
  
  FUNCTION1("f1", KeyType.FUNCTION, 290),
  FUNCTION2("f2", KeyType.FUNCTION, 291),
  FUNCTION3("f3", KeyType.FUNCTION, 292),
  FUNCTION4("f4", KeyType.FUNCTION, 293),
  FUNCTION5("f5", KeyType.FUNCTION, 294),
  FUNCTION6("f6", KeyType.FUNCTION, 295),
  FUNCTION7("f7", KeyType.FUNCTION, 296),
  FUNCTION8("f8", KeyType.FUNCTION, 297),
  FUNCTION9("f9", KeyType.FUNCTION, 298),
  FUNCTION10("f10", KeyType.FUNCTION, 299),
  FUNCTION11("f11", KeyType.FUNCTION, 300),
  FUNCTION12("f12", KeyType.FUNCTION, 301),
  FUNCTION13("f1", KeyType.FUNCTION, 302),
  FUNCTION14("f2", KeyType.FUNCTION, 303),
  FUNCTION15("f3", KeyType.FUNCTION, 304),
  FUNCTION16("f4", KeyType.FUNCTION, 305),
  FUNCTION17("f5", KeyType.FUNCTION, 306),
  FUNCTION18("f6", KeyType.FUNCTION, 307),
  FUNCTION19("f7", KeyType.FUNCTION, 308),
  FUNCTION20("f8", KeyType.FUNCTION, 309),
  FUNCTION21("f9", KeyType.FUNCTION, 310),
  FUNCTION22("f10", KeyType.FUNCTION, 311),
  FUNCTION23("f11", KeyType.FUNCTION, 312),
  FUNCTION24("f12", KeyType.FUNCTION, 313),
  FUNCTION25("f25", KeyType.FUNCTION, 314),
  LEFT_CONTROL("left control", KeyType.SPECIAL, 341),
  LEFT_SHIFT("left shift", KeyType.SPECIAL, 340),
  RIGHT_CONTROL("right control", KeyType.SPECIAL, 345),
  RIGHT_SHIFT("right shift", KeyType.SPECIAL, 344),
  CAPS_LOCK("caps lock", KeyType.SPECIAL, 280),
  TAB("tab", KeyType.SPECIAL, 258),
  LEFT_ALT("left alt", KeyType.SPECIAL, 342),
  RIGHT_ALT("right alt", KeyType.SPECIAL, 346),
  LEFT_SUPER("left super", KeyType.SPECIAL, 343),
  RIGHT_SUPER("right super", KeyType.SPECIAL, 347),
  ENTER("enter", KeyType.SPECIAL, 335),
  BACKSPACE("backspace", KeyType.SPECIAL, 259),
  DELETE("delete", KeyType.SPECIAL, 261),
  UP("up", KeyType.SPECIAL, 265),
  DOWN("down", KeyType.SPECIAL, 264),
  LEFT("left", KeyType.SPECIAL, 263),
  RIGHT("right", KeyType.SPECIAL, 262),
  ESCAPE("escape", KeyType.SPECIAL, 256),
  NONE("none", KeyType.SPECIAL, 1),
  UNKNOWN("unknown", KeyType.SPECIAL, 0);
  
  private final String string;
  private final KeyType type;
  private final int callbackInt;
  private static final Keys[] keyLookup =  new Keys[349];
  public static void init(LoadingScreen loadingScreen) {
    for (Keys key : values()){
      keyLookup[key.callbackInt] = key;
    }
    loadingScreen.increment();
  }
  
  Keys(String string, KeyType keyType, int callbackInt) {
    this.string = string;
    type = keyType;
    this.callbackInt = callbackInt;
  }
  
  public String getString() {
    return string;
  }
  
  public KeyType getType() {
    return type;
  }
  
  public static Keys getKey(int value){
    if (value < 0)return Keys.UNKNOWN;
    return keyLookup[value];
  }
  
  public static Keys fromValue(String value) {
    for (Keys key : Keys.values()) {
      if (value.equalsIgnoreCase(key.string)) {
        return key;
      }
    }
    return null;
  }
}
