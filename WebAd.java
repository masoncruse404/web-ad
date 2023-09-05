import java.awt.*;
import java.awt.geom.*;
import javax.swing.JPanel;
import javax.swing.JFrame;
import java.text.NumberFormat;
import java.math.BigDecimal;
import java.util.Random;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;
class WebAd extends JPanel {


	String slogan;
	String slogan1 = "Pirate Candles & Bath Accessories";
	String slogan2 = "Black Beards Burgers";

	BigDecimal adjustedPrice; 
	BigDecimal unitPrice; 
	BigDecimal decimalDiscountPercent;
	BigDecimal discountRate;
	BigDecimal adjustedPriceBeforeTax; 
	BigDecimal taxRatePercent;
	BigDecimal taxRate;
	String newUnitPrice$, unitPrice$, totalD$, totalBD$, adjustedPrice$, discountRate$, taxRate$;

	private Random r = new Random();

    	NumberFormat currency = NumberFormat.getCurrencyInstance();
    	NumberFormat percent = NumberFormat.getPercentInstance();

	public WebAd() {
		slogan = slogan1;
		randomize();
		if(Math.random() < 0.5)
		{
			slogan = slogan2;
		}
	}

	public void randomize() {
    		double randUnitPrice = (int) ((Math.random() * 90) + 10) / 10.0;
		double subtotal = randUnitPrice;
    		double ranDiscountRate = Math.random() * 49;
    		ranDiscountRate = ranDiscountRate / 100;
		
    		double discountPercent = ranDiscountRate;
    		unitPrice = new BigDecimal(Double.toString(subtotal));
    		unitPrice = unitPrice.setScale(2, RoundingMode.HALF_UP);
    		decimalDiscountPercent = new BigDecimal(Double.toString(discountPercent));
	
    		discountRate = unitPrice.multiply(decimalDiscountPercent);
     		discountRate = discountRate.setScale(2, RoundingMode.HALF_UP);
	
    		adjustedPriceBeforeTax = unitPrice.subtract(discountRate);
    		taxRatePercent = new BigDecimal(".0725");
    		taxRate = taxRatePercent.multiply(adjustedPriceBeforeTax);
    		taxRate = taxRate.setScale(2, RoundingMode.HALF_UP);
    		adjustedPrice = adjustedPriceBeforeTax.add(taxRate);
		adjustedPrice = adjustedPrice.setScale(2, RoundingMode.HALF_UP);
		
		doString();
	}

	public void doString() {
		
		adjustedPrice$ = adjustedPrice.toPlainString();
		newUnitPrice$ = unitPrice.toPlainString();
		// Round Discount Rate to a whole number 
		decimalDiscountPercent = decimalDiscountPercent.multiply(new BigDecimal(100));
    		decimalDiscountPercent = decimalDiscountPercent.setScale(0, RoundingMode.HALF_UP);
		discountRate$ = decimalDiscountPercent.toPlainString(); 

		// Formal sales tax to 2 decimal places
		taxRatePercent = taxRatePercent.multiply(new BigDecimal(100));
		taxRatePercent = taxRatePercent.setScale(2, RoundingMode.HALF_UP);
		taxRate$ = taxRatePercent.toPlainString();
	}

	public String[] getRow() {
		String row[] = {"$" + newUnitPrice$, discountRate$ + "%", taxRate$ + "%", "$" + adjustedPrice$}; 
		return row;
	}

	public String toString() {
		return newUnitPrice$ + "\t" + totalD$ + "\t" + taxRate$ + "\t" + adjustedPrice$;
	}

public void draw(Graphics g, String p) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.setColor(Color.BLACK);

		// Draw Base
		int[] x = {150,150,100};
		int[] y = {100,200,200};
		g2d.drawPolygon(x,y,3);
		
		g2d.setFont(new Font("Merienda", Font.PLAIN, 16));
		g2d.drawString(slogan, 4, 30);
		
		// Drawing Price p
		g2d.setFont(new Font("TimesRoman", Font.PLAIN, 11));
		g2d.drawString("Ad Price: " + p, 2, 230);

		// Draw Sale
		QuadCurve2D q = new QuadCurve2D.Float();
		q.setCurve(160, 90, 290, 170, 160, 200);
		g2d.draw(q);
		QuadCurve2D q2 = new QuadCurve2D.Float();
		q2.setCurve(160, 90, 200, 170, 160, 200);
		g2d.draw(q2);
		
		int[] x2 = {100,120,200,220};
		int[] y2 = {210,240,240,210};
		g2d.drawPolygon(x2,y2,4);
		
		// Draw Sun
		g2d.setColor(Color.YELLOW);
		g2d.fillOval(280,20, 30, 30);

		
		// Draw Water
		g2d.setPaint(Color.BLUE);	
		g2d.fill(new Rectangle2D.Double(0,241,320,79));
}

}



class mypanel extends JPanel
    {
        WebAd boat = new WebAd();
	String price;
        public void paintComponent(Graphics g) {
        super.paintComponent( g );
        boat.draw(g, price);
        }
    }


    
