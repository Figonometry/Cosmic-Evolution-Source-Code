package spacegame.gui;

import spacegame.core.SpaceGame;
import spacegame.render.Assets;
import spacegame.render.Shader;
import spacegame.render.Tessellator;

public final class FontRenderer {
    public int[] glyphMap = new int[256];
    public char[] glyphs = new char[256];
    public static final FontRenderer instance = new FontRenderer();
    public boolean italic = false;


    public FontRenderer() {
        glyphs[15] = '≡';
        glyphs[14] = '±';
        glyphs[13] = '≥';
        glyphs[12] = '≤';
        glyphs[11] = '⌠';
        glyphs[10] = '⌡';
        glyphs[9] = '÷';
        glyphs[8] = '≈';
        glyphs[7] = '°';
        glyphs[6] = '∙';
        glyphs[5] = '·';
        glyphs[4] = '√';
        glyphs[3] = 'ⁿ';
        glyphs[2] = '²';
        glyphs[1] = '■';
        glyphs[0] = ' ';
        glyphs[31] = 'α';
        glyphs[30] = 'ß';
        glyphs[29] = 'Γ';
        glyphs[28] = 'π';
        glyphs[27] = 'Σ';
        glyphs[26] = 'σ';
        glyphs[25] = 'µ';
        glyphs[24] = 'τ';
        glyphs[23] = 'Φ';
        glyphs[22] = 'Θ';
        glyphs[21] = 'Ω';
        glyphs[20] = 'δ';
        glyphs[19] = '∞';
        glyphs[18] = 'φ';
        glyphs[17] = 'ε';
        glyphs[16] = '∩';
        glyphs[47] = '╨';
        glyphs[46] = '╤';
        glyphs[45] = '╥';
        glyphs[44] = '╙';
        glyphs[43] = '╘';
        glyphs[42] = '╒';
        glyphs[41] = '╓';
        glyphs[40] = '╫';
        glyphs[39] = '╪';
        glyphs[38] = '┘';
        glyphs[37] = '┌';
        glyphs[36] = '█';
        glyphs[35] = '▄';
        glyphs[34] = '▌';
        glyphs[33] = '▐';
        glyphs[32] = '▀';
        glyphs[63] = '└';
        glyphs[62] = '┴';
        glyphs[61] = '┬';
        glyphs[60] = '├';
        glyphs[59] = '─';
        glyphs[58] = '┼';
        glyphs[57] = '╞';
        glyphs[56] = '╟';
        glyphs[55] = '╚';
        glyphs[54] = '╔';
        glyphs[53] = '╩';
        glyphs[52] = '╦';
        glyphs[51] = '╠';
        glyphs[50] = '═';
        glyphs[49] = '╬';
        glyphs[48] = '╧';
        glyphs[79] = '░';
        glyphs[78] = '▒';
        glyphs[77] = '▓';
        glyphs[76] = '│';
        glyphs[75] = '┤';
        glyphs[74] = '╡';
        glyphs[73] = '╢';
        glyphs[72] = '╖';
        glyphs[71] = '╕';
        glyphs[70] = '╣';
        glyphs[69] = '║';
        glyphs[68] = '╗';
        glyphs[67] = '╝';
        glyphs[66] = '╜';
        glyphs[65] = '╛';
        glyphs[64] = '┐';
        glyphs[95] = 'á';
        glyphs[94] = 'í';
        glyphs[93] = 'ó';
        glyphs[92] = 'ú';
        glyphs[91] = 'ñ';
        glyphs[90] = 'Ñ';
        glyphs[89] = 'ª';
        glyphs[88] = 'º';
        glyphs[87] = '¿';
        glyphs[86] = '⌐';
        glyphs[85] = '¬';
        glyphs[84] = '½';
        glyphs[83] = '¼';
        glyphs[82] = '¡';
        glyphs[81] = '«';
        glyphs[80] = '»';
        glyphs[111] = 'É';
        glyphs[110] = 'æ';
        glyphs[109] = 'Æ';
        glyphs[108] = 'ô';
        glyphs[107] = 'ö';
        glyphs[106] = 'ò';
        glyphs[105] = 'û';
        glyphs[104] = 'ù';
        glyphs[103] = 'ÿ';
        glyphs[102] = 'Ö';
        glyphs[101] = 'Ü';
        glyphs[100] = '¢';
        glyphs[99] = '£';
        glyphs[98] = '¥';
        glyphs[97] = '₧';
        glyphs[96] = 'ƒ';
        glyphs[127] = 'Ç';
        glyphs[126] = 'ü';
        glyphs[125] = 'é';
        glyphs[124] = 'â';
        glyphs[123] = 'ä';
        glyphs[122] = 'à';
        glyphs[121] = 'å';
        glyphs[120] = 'ç';
        glyphs[119] = 'ê';
        glyphs[118] = 'ë';
        glyphs[117] = 'è';
        glyphs[116] = 'ï';
        glyphs[115] = 'î';
        glyphs[114] = 'ì';
        glyphs[113] = 'Ä';
        glyphs[112] = 'Å';
        glyphs[143] = 'p';
        glyphs[142] = 'q';
        glyphs[141] = 'r';
        glyphs[140] = 's';
        glyphs[139] = 't';
        glyphs[138] = 'u';
        glyphs[137] = 'v';
        glyphs[136] = 'w';
        glyphs[135] = 'x';
        glyphs[134] = 'y';
        glyphs[133] = 'z';
        glyphs[132] = '{';
        glyphs[131] = '¦';
        glyphs[130] = '}';
        glyphs[129] = '~';
        glyphs[128] = '⌂';
        glyphs[159] = '`';
        glyphs[158] = 'a';
        glyphs[157] = 'b';
        glyphs[156] = 'c';
        glyphs[155] = 'd';
        glyphs[154] = 'e';
        glyphs[153] = 'f';
        glyphs[152] = 'g';
        glyphs[151] = 'h';
        glyphs[150] = 'i';
        glyphs[149] = 'j';
        glyphs[148] = 'k';
        glyphs[147] = 'l';
        glyphs[146] = 'm';
        glyphs[145] = 'n';
        glyphs[144] = 'o';
        glyphs[175] = 'P';
        glyphs[174] = 'Q';
        glyphs[173] = 'R';
        glyphs[172] = 'S';
        glyphs[171] = 'T';
        glyphs[170] = 'U';
        glyphs[169] = 'V';
        glyphs[168] = 'W';
        glyphs[167] = 'X';
        glyphs[166] = 'Y';
        glyphs[165] = 'Z';
        glyphs[164] = '[';
        glyphs[163] = '\\';
        glyphs[162] = ']';
        glyphs[161] = '^';
        glyphs[160] = '_';
        glyphs[191] = '@';
        glyphs[190] = 'A';
        glyphs[189] = 'B';
        glyphs[188] = 'C';
        glyphs[187] = 'D';
        glyphs[186] = 'E';
        glyphs[185] = 'F';
        glyphs[184] = 'G';
        glyphs[183] = 'H';
        glyphs[182] = 'I';
        glyphs[181] = 'J';
        glyphs[180] = 'K';
        glyphs[179] = 'L';
        glyphs[178] = 'M';
        glyphs[177] = 'N';
        glyphs[176] = 'O';
        glyphs[207] = '0';
        glyphs[206] = '1';
        glyphs[205] = '2';
        glyphs[204] = '3';
        glyphs[203] = '4';
        glyphs[202] = '5';
        glyphs[201] = '6';
        glyphs[200] = '7';
        glyphs[199] = '8';
        glyphs[198] = '9';
        glyphs[197] = ':';
        glyphs[196] = ';';
        glyphs[195] = '<';
        glyphs[194] = '=';
        glyphs[193] = '>';
        glyphs[192] = '?';
        glyphs[223] = ' ';
        glyphs[222] = '!';
        glyphs[221] = '"';
        glyphs[220] = '#';
        glyphs[219] = '$';
        glyphs[218] = '%';
        glyphs[217] = '&';
        glyphs[216] = '\'';
        glyphs[215] = '(';
        glyphs[214] = ')';
        glyphs[213] = '*';
        glyphs[212] = '+';
        glyphs[211] = ',';
        glyphs[210] = '-';
        glyphs[209] = '.';
        glyphs[208] = '/';
        glyphs[239] = '►';
        glyphs[238] = '◄';
        glyphs[237] = '↕';
        glyphs[236] = '‼';
        glyphs[235] = '¶';
        glyphs[234] = '§';
        glyphs[233] = '▬';
        glyphs[232] = '↨';
        glyphs[231] = '↑';
        glyphs[230] = '↓';
        glyphs[229] = '→';
        glyphs[228] = '←';
        glyphs[227] = '∟';
        glyphs[226] = '↔';
        glyphs[225] = '▲';
        glyphs[224] = '▼';
        glyphs[255] = '□';
        glyphs[254] = '☺';
        glyphs[253] = '☻';
        glyphs[252] = '♥';
        glyphs[251] = '♦';
        glyphs[250] = '♣';
        glyphs[249] = '♠';
        glyphs[248] = '•';
        glyphs[247] = '◘';
        glyphs[246] = '○';
        glyphs[245] = '◙';
        glyphs[244] = '♂';
        glyphs[243] = '♀';
        glyphs[242] = '♪';
        glyphs[241] = '♫';
        glyphs[240] = '☼';


        for (int i = 0; i <= 255; i++) {
            glyphMap[i] = i;
        }
    }


    public void drawString(String string, float x, float y, float depth, int color, int font) {
        char[] stringChars = new char[string.length()];
        int[] stringGlpyhIndex = new int[stringChars.length];

        for (int i = 0; i <= string.length() - 1; i++) {
            stringChars[i] = string.charAt(i);
        }

        for (int i = 0; i <= 255; i++) {
            for (int j = 0; j <= stringChars.length - 1; j++) {
                if (glyphs[i] == stringChars[j]) {
                    stringGlpyhIndex[j] = i;
                }
            }
        }
        this.generateStringVertexData(stringGlpyhIndex, x, y, depth,color, font);
        this.generateStringVertexDataShadow(stringGlpyhIndex, x+3, y-3, depth-3,font);
    }

    public void drawCenteredString(String string, float x, float y, float depth, int color, int font) {
        final float spacingSizePerFont = 0.34f;
        x -= string.length() * (font * spacingSizePerFont)/2F;
        char[] stringChars = new char[string.length()];
        int[] stringGlpyhIndex = new int[stringChars.length];

        for (int i = 0; i <= string.length() - 1; i++) {
            stringChars[i] = string.charAt(i);
        }

        for (int i = 0; i <= 255; i++) {
            for (int j = 0; j <= stringChars.length - 1; j++) {
                if (glyphs[i] == stringChars[j]) {
                    stringGlpyhIndex[j] = i;
                }
            }
        }
        this.generateStringVertexData(stringGlpyhIndex, x, y, depth,color,font);
        this.generateStringVertexDataShadow(stringGlpyhIndex, x+3, y-3, depth-1,font);
    }


    public void drawString(boolean value, int x, int y, int depth, int color, int font) {
        drawString(Boolean.toString(value), x, y, color, font, depth);
    }

    public void drawString(char character, int x, int y, int depth, int color, int font) {
        drawString(Character.toString(character), x, y, color, font, depth);
    }

    public void drawString(byte value, int x, int y, int depth,  int color, int font) {
        drawString(Byte.toString(value), x, y, color, font, depth);
    }

    public void drawString(short value, int x, int y, int depth,  int color, int font) {
        drawString(Short.toString(value), x, y, color, font, depth);
    }

    public void drawString(int value, int x, int y, int depth,  int color, int font) {
        drawString(Integer.toString(value), x, y, color, font, depth);
    }

    public void drawString(long value, int x, int y, int depth,  int color, int font) {
        drawString(Long.toString(value), x, y, color, font, depth);
    }

    public void drawString(double value, int x, int y, int depth,  int color, int font) {
        drawString(Double.toString(value), x, y, color, font, depth);
    }

    public void drawString(float value, int x, int y, int depth,  int color, int font) {
        drawString(Float.toString(value), x, y, color, font, depth);
    }



    private void generateStringVertexData(int[] stringGlyphIndex, float x, float y, float depth, int color, int font) {
        Tessellator tessellator = Tessellator.instance;
        final float spacingSizePerFont = 0.34f;
        int numGlyphs = stringGlyphIndex.length;
        for (int i = 0; i < numGlyphs; i++) {
            if(this.italic){
                tessellator.addVertex2DTextureWithAtlas(color, x + font, y, depth, 3, Assets.fontTextureAtlas.textures.get(stringGlyphIndex[i]), stringGlyphIndex[i]/16F);
                tessellator.addVertex2DTextureWithAtlas(color, x + (font/4f), y + font, depth, 1, Assets.fontTextureAtlas.textures.get(stringGlyphIndex[i]), stringGlyphIndex[i]/16F);
                tessellator.addVertex2DTextureWithAtlas(color, x + font + (font/4f), y + font, depth, 2, Assets.fontTextureAtlas.textures.get(stringGlyphIndex[i]), stringGlyphIndex[i]/16F);
                tessellator.addVertex2DTextureWithAtlas(color, x, y, depth, 0, Assets.fontTextureAtlas.textures.get(stringGlyphIndex[i]), stringGlyphIndex[i]/16F);
                tessellator.addElements();
                x += font * spacingSizePerFont;
            } else {
                tessellator.addVertex2DTextureWithAtlas(color, x + font, y, depth, 3, Assets.fontTextureAtlas.textures.get(stringGlyphIndex[i]), stringGlyphIndex[i]/16F);
                tessellator.addVertex2DTextureWithAtlas(color, x, y + font, depth, 1, Assets.fontTextureAtlas.textures.get(stringGlyphIndex[i]), stringGlyphIndex[i]/16F);
                tessellator.addVertex2DTextureWithAtlas(color, x + font, y + font, depth, 2, Assets.fontTextureAtlas.textures.get(stringGlyphIndex[i]), stringGlyphIndex[i]/16F);
                tessellator.addVertex2DTextureWithAtlas(color, x, y, depth, 0, Assets.fontTextureAtlas.textures.get(stringGlyphIndex[i]), stringGlyphIndex[i]/16F);
                tessellator.addElements();
                x += font * spacingSizePerFont;
            }
        }
        tessellator.toggleOrtho();
        tessellator.drawTexture2DWithAtlas(Assets.fontTextureLoader.texID, Shader.screen2DTextureAtlas, SpaceGame.camera);
        tessellator.toggleOrtho();
    }

    private void generateStringVertexDataShadow(int[] stringGlyphIndex, float x, float y, float depth, int font) {
        Tessellator tessellator = Tessellator.instance;
        final float spacingSizePerFont = 0.34f;
        int numGlyphs = stringGlyphIndex.length;
        for (int i = 0; i < numGlyphs; i++) {
            if(this.italic){
                tessellator.addVertex2DTextureWithAtlas(4210752, x + font, y, depth, 3, Assets.fontTextureAtlas.textures.get(stringGlyphIndex[i]), stringGlyphIndex[i]/16F);
                tessellator.addVertex2DTextureWithAtlas(4210752, x + (font/4f), y + font, depth, 1, Assets.fontTextureAtlas.textures.get(stringGlyphIndex[i]), stringGlyphIndex[i]/16F);
                tessellator.addVertex2DTextureWithAtlas(4210752, x + font + (font/4f), y + font, depth, 2, Assets.fontTextureAtlas.textures.get(stringGlyphIndex[i]), stringGlyphIndex[i]/16F);
                tessellator.addVertex2DTextureWithAtlas(4210752, x, y, -17, 0, Assets.fontTextureAtlas.textures.get(stringGlyphIndex[i]), stringGlyphIndex[i]/16F);
                tessellator.addElements();
                x += font * spacingSizePerFont;
            } else {
                tessellator.addVertex2DTextureWithAtlas(4210752, x + font, y, depth, 3, Assets.fontTextureAtlas.textures.get(stringGlyphIndex[i]), stringGlyphIndex[i] / 16F);
                tessellator.addVertex2DTextureWithAtlas(4210752, x, y + font, depth, 1, Assets.fontTextureAtlas.textures.get(stringGlyphIndex[i]), stringGlyphIndex[i] / 16F);
                tessellator.addVertex2DTextureWithAtlas(4210752, x + font, y + font, depth, 2, Assets.fontTextureAtlas.textures.get(stringGlyphIndex[i]), stringGlyphIndex[i] / 16F);
                tessellator.addVertex2DTextureWithAtlas(4210752, x, y, depth, 0, Assets.fontTextureAtlas.textures.get(stringGlyphIndex[i]), stringGlyphIndex[i] / 16F);
                tessellator.addElements();
                x += font * spacingSizePerFont;
            }
        }
        tessellator.toggleOrtho();
        tessellator.drawTexture2DWithAtlas(Assets.fontTextureLoader.texID, Shader.screen2DTextureAtlas, SpaceGame.camera);
        tessellator.toggleOrtho();
    }


    public void toggleItalics(){
        this.italic = !this.italic;
    }

}

