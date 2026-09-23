 package su.sacura.util.impl.system;
 
 import java.util.HashMap;
 import java.util.Map;
 
 public class KeyUtil {
   private static final Map<String, Integer> keyMap = new HashMap<>();
   
   private static final Map<Integer, String> keyString = new HashMap<>();
   
   public static String getKey(int key) {
     return getStringKey(key);
   }
   
   public static String getStringKey(int key) {
     if (key == -1)
       return "n/a"; 
     return keyString.getOrDefault(Integer.valueOf(key), "n/a");
   }
   
   private static void keyString() {
     for (Map.Entry<String, Integer> f : keyMap.entrySet())
       keyString.put(f.getValue(), f.getKey()); 
   }
   
   static {
     putKey();
     keyString();
   }
   
   private static void putKey() {
     keyMap.put("Mouse1", Integer.valueOf(0));
     keyMap.put("Mouse2", Integer.valueOf(1));
     keyMap.put("Mouse3", Integer.valueOf(2));
     keyMap.put("Mouse4", Integer.valueOf(3));
     keyMap.put("Mouse5", Integer.valueOf(4));
     keyMap.put("Mouse6", Integer.valueOf(5));
     keyMap.put("Mouse7", Integer.valueOf(6));
     keyMap.put("Mouse8", Integer.valueOf(7));
     keyMap.put("@", Integer.valueOf(64));
     keyMap.put("a", Integer.valueOf(65));
     keyMap.put("b", Integer.valueOf(66));
     keyMap.put("c", Integer.valueOf(67));
     keyMap.put("d", Integer.valueOf(68));
     keyMap.put("e", Integer.valueOf(69));
     keyMap.put("f", Integer.valueOf(70));
     keyMap.put("g", Integer.valueOf(71));
     keyMap.put("h", Integer.valueOf(72));
     keyMap.put("i", Integer.valueOf(73));
     keyMap.put("j", Integer.valueOf(74));
     keyMap.put("k", Integer.valueOf(75));
     keyMap.put("l", Integer.valueOf(76));
     keyMap.put("m", Integer.valueOf(77));
     keyMap.put("n", Integer.valueOf(78));
     keyMap.put("o", Integer.valueOf(79));
     keyMap.put("p", Integer.valueOf(80));
     keyMap.put("q", Integer.valueOf(81));
     keyMap.put("r", Integer.valueOf(82));
     keyMap.put("s", Integer.valueOf(83));
     keyMap.put("t", Integer.valueOf(84));
     keyMap.put("u", Integer.valueOf(85));
     keyMap.put("v", Integer.valueOf(86));
     keyMap.put("w", Integer.valueOf(87));
     keyMap.put("x", Integer.valueOf(88));
     keyMap.put("y", Integer.valueOf(89));
     keyMap.put("z", Integer.valueOf(90));
     keyMap.put("0", Integer.valueOf(48));
     keyMap.put("1", Integer.valueOf(49));
     keyMap.put("2", Integer.valueOf(50));
     keyMap.put("3", Integer.valueOf(51));
     keyMap.put("4", Integer.valueOf(52));
     keyMap.put("5", Integer.valueOf(53));
     keyMap.put("6", Integer.valueOf(54));
     keyMap.put("7", Integer.valueOf(55));
     keyMap.put("8", Integer.valueOf(56));
     keyMap.put("9", Integer.valueOf(57));
     keyMap.put("F1", Integer.valueOf(290));
     keyMap.put("F2", Integer.valueOf(291));
     keyMap.put("F3", Integer.valueOf(292));
     keyMap.put("F4", Integer.valueOf(293));
     keyMap.put("F5", Integer.valueOf(294));
     keyMap.put("F6", Integer.valueOf(295));
     keyMap.put("F7", Integer.valueOf(296));
     keyMap.put("F8", Integer.valueOf(297));
     keyMap.put("F9", Integer.valueOf(298));
     keyMap.put("F10", Integer.valueOf(299));
     keyMap.put("F11", Integer.valueOf(300));
     keyMap.put("F12", Integer.valueOf(301));
     keyMap.put("LBracket", Integer.valueOf(91));
     keyMap.put("RBracket", Integer.valueOf(93));
     keyMap.put("Num1", Integer.valueOf(321));
     keyMap.put("Num2", Integer.valueOf(322));
     keyMap.put("Num3", Integer.valueOf(323));
     keyMap.put("Num4", Integer.valueOf(324));
     keyMap.put("Num5", Integer.valueOf(325));
     keyMap.put("Num6", Integer.valueOf(326));
     keyMap.put("Num7", Integer.valueOf(327));
     keyMap.put("Num8", Integer.valueOf(328));
     keyMap.put("Num9", Integer.valueOf(329));
     keyMap.put("Space", Integer.valueOf(32));
     keyMap.put("Enter", Integer.valueOf(257));
     keyMap.put("Escape", Integer.valueOf(256));
     keyMap.put("Home", Integer.valueOf(268));
     keyMap.put("Insert", Integer.valueOf(260));
     keyMap.put("Delete", Integer.valueOf(261));
     keyMap.put("End", Integer.valueOf(269));
     keyMap.put("PageUp", Integer.valueOf(266));
     keyMap.put("PageDown", Integer.valueOf(267));
     keyMap.put("right", Integer.valueOf(262));
     keyMap.put("Left", Integer.valueOf(263));
     keyMap.put("Down", Integer.valueOf(264));
     keyMap.put("Up", Integer.valueOf(265));
     keyMap.put("RShift", Integer.valueOf(344));
     keyMap.put("LShift", Integer.valueOf(340));
     keyMap.put("RControl", Integer.valueOf(345));
     keyMap.put("LControl", Integer.valueOf(341));
     keyMap.put("RAlt", Integer.valueOf(346));
     keyMap.put("LAlt", Integer.valueOf(342));
     keyMap.put("RSuper", Integer.valueOf(347));
     keyMap.put("LSuper", Integer.valueOf(343));
     keyMap.put("Menu", Integer.valueOf(348));
     keyMap.put("CapsLock", Integer.valueOf(280));
     keyMap.put("NumLock", Integer.valueOf(282));
     keyMap.put("ScrollLock", Integer.valueOf(281));
     keyMap.put("KPDecimal", Integer.valueOf(330));
     keyMap.put("KPDivide", Integer.valueOf(331));
     keyMap.put("KPMultiply", Integer.valueOf(332));
     keyMap.put("KPSubtract", Integer.valueOf(333));
     keyMap.put("KPPlus", Integer.valueOf(334));
     keyMap.put("KPEnter", Integer.valueOf(335));
     keyMap.put("KPEqual", Integer.valueOf(336));
     keyMap.put("Apostrophe", Integer.valueOf(39));
     keyMap.put("Slash", Integer.valueOf(47));
     keyMap.put("Minus", Integer.valueOf(45));
     keyMap.put("Plus", Integer.valueOf(61));
     keyMap.put("Back", Integer.valueOf(259));
     keyMap.put("Backslash", Integer.valueOf(92));
     keyMap.put("Period", Integer.valueOf(46));
     keyMap.put("Comma", Integer.valueOf(44));
     keyMap.put("Pause", Integer.valueOf(284));
     keyMap.put("`", Integer.valueOf(96));
   }
 }


