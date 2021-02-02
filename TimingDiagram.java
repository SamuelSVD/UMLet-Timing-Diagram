import java.awt.*;
import java.util.*;

import com.baselet.control.constants.Constants;
import com.baselet.control.util.Utils;
import com.baselet.element.old.custom.CustomElement;

@SuppressWarnings("serial")
public class <!CLASSNAME!> extends CustomElement {

	public CustomElementImpl() {
		
	}
	
	@Override
	public void paint() {
		Vector<String> textlines = Utils.decomposeStrings(this.getPanelAttributes());
	
		/****CUSTOM_CODE START****/
//This is an element that generates a timing diagram.
//example:
/*
title=Sequence Time Diagram
x_offset=80
Step:char
InAuto
InProcess
IsWaiting
tick
0000
0100
1110
1111
2110
2111
3110
3111
4110
4111
0100
0100
0100
1110
1111
2110
2111
3110
3111
4110
4111
0100
0100
*/
String title = "";

int y=textHeight();
String labels[] = new String[100];
boolean isChar[] = new boolean[100];
int numLabels = 0;

int values[][] = new int[100][100];
int maxValue[] = new int[100];
int numValues = 0;
int timeWidth = 20;
int x_offset = 20;
boolean draw_step = false;
boolean doneInit = false;
drawRectangle(0,0,width,height);
setWordWrap(true);

for(String textline : textlines) {
	if (doneInit) {
		for (int j = 0; j < textline.length(); j++) {
			values[j][numValues] = textline.charAt(j) - 48;
		}
		numValues++;
	} else {
		if (textline.equals("tick")) {
			doneInit = true;
		}
		else if (textline.toLowerCase().startsWith("title=")) {
			title=textline.substring(6);
		}
		else if (textline.toLowerCase().startsWith("timewidth=")) {
			timeWidth = Integer.parseInt(textline.substring(10));
		}
		else if (textline.toLowerCase().startsWith("x_offset=")) {
			x_offset = Integer.parseInt(textline.substring(9));
		}
		else if (textline.toLowerCase().startsWith("draw_step=")) {
			draw_step = 1 == Integer.parseInt(textline.substring(10));
		}
		else {
			if (textline.endsWith(":char")) {
				isChar[numLabels] = true;
				textline = textline.replace(":char", "");
			}
			labels[numLabels] = textline;
			numLabels++;
		}
	}
}

printLeft(title, y);

for (int i = 0; i < numLabels; i++) {
	printLeft(labels[i], y*(i*2+3)-3);
}

if (draw_step) {
	setForegroundColor("gray");
	setLineType(2);
	for (int i = 0; i <= numValues; i++) {
		int top = y * (0*2+2);
		int bot = y * ((numLabels-1) * 2 + 3);
		drawLine(x_offset + timeWidth * i, top, x_offset + timeWidth * i, bot);
	}
	setForegroundColor("black");
	setLineType(0);
}

for (int i = 0; i < numLabels; i++) {
	int one =  y * (i*2+2);
	int zero = y * (i*2+3);
	for (int j = 0; j < numValues; j++) {
		if (j > 0) {
			if (values[i][j-1] != values[i][j]) {
				if (isChar[i]) {
					print(String.valueOf(values[i][j]),x_offset + j*timeWidth + 2, y*(i*2+3)-3);
				}
				drawLine(x_offset + timeWidth * j, one, x_offset + timeWidth * j, zero);
			}
		} else {
			if (isChar[i]) {
				print(String.valueOf(values[i][j]),x_offset + j*timeWidth + 2, y*(i*2+3)-3);
				drawLine(x_offset + timeWidth * j, one, x_offset + timeWidth * j, zero);
			}
		}
		if (values[i][j] == 0 || isChar[i]) {
			drawLine(x_offset + timeWidth * j, zero, x_offset + timeWidth * (j+1), zero);
		}
		if (values[i][j] != 0 || isChar[i]) {
			drawLine(x_offset + timeWidth * j, one, x_offset + timeWidth * (j+1), one);
		}
	}
	if (isChar[i]) {
		drawLine(x_offset + timeWidth * numValues, one, x_offset + timeWidth * numValues, zero);
	}

}

		


		

		/****CUSTOM_CODE END****/
	}
}
