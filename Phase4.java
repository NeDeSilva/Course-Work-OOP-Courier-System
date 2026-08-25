import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * Phase4.java
 * Generates high-fidelity mockup SVGs and prints visual specs to stdout.
 * Run this class to emit several SVG files and a visual-spec.txt in the project root.
 */
public class Phase4 {
    private static final String OUTPUT_DIR = ".";

    // Visual palette
    private static final String PRIMARY = "#3498db"; // blue
    private static final String PRIMARY_DARK = "#2c80b4";
    private static final String ACCENT = "#27ae60"; // green
    private static final String BG = "#f5f7fa";
    private static final String CARD = "#ffffff";
    private static final String TEXT = "#222f3a";
    private static final String MUTED = "#7b8a95";

    public static void main(String[] args) throws Exception {
        System.out.println("Phase 4 — High-fidelity mockups & visual spec\nGenerated: " + LocalDateTime.now());
        writeVisualSpec();
        writeSvg("mockup-dashboard.svg", dashboardSvg());
        writeSvg("mockup-inventory.svg", inventorySvg());
        writeSvg("mockup-users.svg", usersSvg());
        writeSvg("mockup-create-shipment.svg", createShipmentSvg());
        writeSvg("mockup-delivery.svg", deliverySvg());

        System.out.println("Generated files:");
        System.out.println(" - mockup-dashboard.svg");
        System.out.println(" - mockup-inventory.svg");
        System.out.println(" - mockup-users.svg");
        System.out.println(" - mockup-create-shipment.svg");
        System.out.println(" - mockup-delivery.svg");
        System.out.println(" - visual-spec.txt");

        System.out.println("\nOpen the SVG files in a browser or editor to view the annotated mockups.");
    }

    private static void writeVisualSpec() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Phase 4 — Visual specification\n");
        sb.append("Generated: ").append(LocalDateTime.now()).append("\n\n");
        sb.append("Color palette:\n");
        sb.append("- Primary: " + PRIMARY + " (primary actions, header)\n");
        sb.append("- Primary dark: " + PRIMARY_DARK + " (hover / active)\n");
        sb.append("- Accent: " + ACCENT + " (success, badges)\n");
        sb.append("- Background: " + BG + " (app background)\n");
        sb.append("- Card: " + CARD + " (panels)\n");
        sb.append("- Text: " + TEXT + " (main text)\n");
        sb.append("- Muted: " + MUTED + " (secondary text, placeholders)\n\n");

        sb.append("Typography:\n");
        sb.append("- Typeface: System UI / Sans-serif (UI). Fallback: Arial, Helvetica.\n");
        sb.append("- Sizes (desktop):");
        sb.append(" h1: 28px, h2: 20px, body: 14px, small: 12px\n\n");

        sb.append("Spacing system:\n");
        sb.append("- Base unit: 8px. Use multiples for gap, padding, margin (8,16,24,32).\n\n");

        sb.append("Components spec:\n");
        sb.append("- Button (primary): background=PRIMARY, color=#fff, padding=8px 14px, border-radius=6px, hover=PRIMARY_DARK.\n");
        sb.append("- Button (secondary): background=#fff, border=1px solid #dfe6eb, color=TEXT.\n");
        sb.append("- Table: zebra rows optional, header bold 12px, row height 36px, selectable with focus ring.\n");
        sb.append("- Inputs: height 34px, padding 8px, border-radius 6px, border 1px solid #dfe6eb. Invalid border color: #e74c3c.\n");
        sb.append("- Modal: centered card, max-width 720px, overlay rgba(0,0,0,0.35), primary action on right.\n\n");

        sb.append("Responsive notes:\n");
        sb.append("- Desktop: 1200–1600px, full layout with left/right panes.\n");
        sb.append("- Narrow widths (< 900px): convert right pane to stacked below list; collapse complex KPIs into summary cards.\n");
        sb.append("- Use fluid widths for tables with horizontal scroll at small sizes.\n\n");

        sb.append("Annotations: mockup SVGs include callouts showing component usage and spacing.\n");

        Path out = Paths.get(OUTPUT_DIR, "visual-spec.txt");
        Files.writeString(out, sb.toString(), StandardCharsets.UTF_8);
    }

    private static void writeSvg(String filename, String content) throws IOException {
        Path out = Paths.get(OUTPUT_DIR, filename);
        Files.writeString(out, content, StandardCharsets.UTF_8);
    }

    private static String svgHeader(int w, int h) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
               "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"" + w + "\" height=\"" + h + "\" viewBox=\"0 0 " + w + " " + h + "\">\n" +
               "<style>text{font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, 'Helvetica Neue', Arial; fill: " + TEXT + ";}</style>\n";
    }

    private static String dashboardSvg() {
        int W = 1200, H = 800;
        StringBuilder s = new StringBuilder();
        s.append(svgHeader(W, H));
        // background
        s.append("<rect width=\"100%\" height=\"100%\" fill=\"" + BG + "\"/>\n");
        // header
        s.append("<rect x=\"0\" y=\"0\" width=\"100%\" height=\"72\" fill=\"" + CARD + "\" stroke=\"#e1e7ee\"/>\n");
        s.append("<text x=\"20\" y=\"44\" font-size=\"20\" font-weight=\"700\">Courier Management System</text>\n");
        s.append("<rect x=\"940\" y=\"16\" width=\"180\" height=\"40\" rx=\"6\" fill=\"" + PRIMARY + "\"/>\n");
        s.append("<text x=\"980\" y=\"42\" font-size=\"14\" fill=\"#ffffff\">Save</text>\n");
        // tabs
        s.append("<g transform=\"translate(0,80)\">\n");
        String[] tabs = {"Dashboard","Inventory","Users","Orders","Reports","Settings"};
        int tx = 20;
        for (int i=0;i<tabs.length;i++){
            s.append("<text x=\""+tx+"\" y=\"24\" font-size=\"16\" fill=\""+(i==0?TEXT:MUTED)+"\">"+tabs[i]+"</text>\n");
            tx += 140;
        }
        s.append("</g>\n");
        // KPI cards
        int kx = 20, ky = 140, kw = 220, kh = 80, gap = 16;
        String[] klabels = {"Total Orders","Pending","In Transit","Delivered"};
        String[] kvals = {"124","12","14","98"};
        for (int i=0;i<klabels.length;i++){
            int x = kx + i*(kw+gap);
            s.append("<rect x=\""+x+"\" y=\""+ky+"\" width=\""+kw+"\" height=\""+kh+"\" rx=\"8\" fill=\""+CARD+"\" stroke=\"#e6eef5\"/>\n");
            s.append("<text x=\""+(x+16)+"\" y=\""+(ky+34)+" font-size=\"20\" font-weight=\"700\">"+kvals[i]+"</text>\n");
            s.append("<text x=\""+(x+16)+"\" y=\""+(ky+56)+" font-size=\"12\" fill=\""+MUTED+"\">"+klabels[i]+"</text>\n");
        }
        // activity column
        s.append("<rect x=\"740\" y=\"140\" width=\"420\" height=\"420\" rx=\"8\" fill=\""+CARD+"\" stroke=\"#e6eef5\"/>\n");
        s.append("<text x=\"760\" y=\"164\" font-size=\"14\" font-weight=\"700\">Recent Activity</text>\n");
        s.append("<text x=\"760\" y=\"188\" font-size=\"12\" fill=\""+MUTED+"\">10:30 Seller A added item IT-200</text>\n");
        s.append("<text x=\"760\" y=\"208\" font-size=\"12\" fill=\""+MUTED+"\">09:45 Driver D marked delivery ORD-102</text>\n");
        // footer status
        s.append("<rect x=\"20\" y=\"680\" width=\"1160\" height=\"80\" rx=\"6\" fill=\""+CARD+"\" stroke=\"#e6eef5\"/>\n");
        s.append("<text x=\"40\" y=\"720\" font-size=\"12\" fill=\""+MUTED+"\">Saved 2m ago | Last action: Add Item</text>\n");
        s.append("</svg>");
        return s.toString();
    }

    private static String inventorySvg() {
        int W=1200,H=800; StringBuilder s=new StringBuilder(); s.append(svgHeader(W,H));
        s.append("<rect width=\"100%\" height=\"100%\" fill=\""+BG+"\"/>\n");
        s.append("<text x=\"20\" y=\"40\" font-size=\"20\" font-weight=\"700\">Inventory</text>\n");
        s.append("<rect x=\"20\" y=\"60\" width=\"1160\" height=\"44\" rx=\"6\" fill=\"#fff\" stroke=\"#e6eef5\"/>\n");
        s.append("<text x=\"30\" y=\"88\" font-size=\"13\" fill=\""+MUTED+"\">Search | Filters | Sort | + Add Item</text>\n");
        // table header
        s.append("<rect x=\"20\" y=\"120\" width=\"1160\" height=\"40\" fill=\"#f8fbff\" stroke=\"#e6eef5\"/>\n");
        s.append("<text x=\"34\" y=\"146\" font-size=\"13\" font-weight=\"700\">ID</text>");
        s.append("<text x=\"120\" y=\"146\" font-size=\"13\" font-weight=\"700\">Name</text>");
        s.append("<text x=\"420\" y=\"146\" font-size=\"13\" font-weight=\"700\">Price</text>");
        s.append("<text x=\"520\" y=\"146\" font-size=\"13\" font-weight=\"700\">Stock</text>\n");
        // sample rows
        int ry=164;
        for(int i=0;i<6;i++){
            s.append("<rect x=\"20\" y=\""+(ry+i*40)+"\" width=\"1160\" height=\"40\" fill=\"#ffffff\" stroke=\"#f1f5f8\"/>\n");
            s.append("<text x=\"34\" y=\""+(ry+24+i*40)+"\" font-size=\"13\">IT-"+(100+i)+"</text>\n");
            s.append("<text x=\"120\" y=\""+(ry+24+i*40)+"\" font-size=\"13\">Sample Item "+(i+1)+"</text>\n");
            s.append("<text x=\"420\" y=\""+(ry+24+i*40)+"\" font-size=\"13\">"+(750+i*50)+"</text>\n");
            s.append("<text x=\"520\" y=\""+(ry+24+i*40)+"\" font-size=\"13\">"+(20-i)+"</text>\n");
            s.append("<rect x=\"980\" y=\""+(ry+10+i*40)+"\" width=\"80\" height=\"24\" rx=\"4\" fill=\"#fff\" stroke=\"#dfe6eb\"/>\n");
            s.append("<text x=\"994\" y=\""+(ry+26+i*40)+"\" font-size=\"12\">Edit</text>\n");
        }
        // inline add form annotation
        s.append("<rect x=\"20\" y=\"420\" width=\"1160\" height=\"110\" rx=\"8\" fill=\""+CARD+"\" stroke=\"#e6eef5\"/>\n");
        s.append("<text x=\"40\" y=\"444\" font-size=\"14\" font-weight=\"700\">Inline Add Item</text>\n");
        s.append("<text x=\"40\" y=\"468\" font-size=\"12\" fill=\""+MUTED+"\">[ID*] [Name*] [Price*] [Stock*] [Add] [Cancel]</text>\n");
        s.append("</svg>");
        return s.toString();
    }

    private static String usersSvg() {
        int W=1200,H=800; StringBuilder s=new StringBuilder(); s.append(svgHeader(W,H));
        s.append("<rect width=\"100%\" height=\"100%\" fill=\""+BG+"\"/>\n");
        s.append("<text x=\"20\" y=\"40\" font-size=\"20\" font-weight=\"700\">Users</text>\n");
        s.append("<rect x=\"20\" y=\"60\" width=\"1160\" height=\"44\" rx=\"6\" fill=\"#fff\" stroke=\"#e6eef5\"/>\n");
        s.append("<text x=\"30\" y=\"88\" font-size=\"13\" fill=\""+MUTED+"\">Role: [All v]  Search: [_____]  [Add User]</text>\n");
        // left list
        s.append("<rect x=\"20\" y=\"120\" width=\"360\" height=\"560\" rx=\"8\" fill=\""+CARD+"\" stroke=\"#e6eef5\"/>\n");
        s.append("<text x=\"40\" y=\"144\" font-size=\"14\" font-weight=\"700\">Users (list)</text>\n");
        for(int i=0;i<8;i++){
            s.append("<rect x=\"32\" y=\"160\" width=\"336\" height=\"32\" fill=\"#fff\" stroke=\"#f1f5f8\"/>\n");
            s.append("<text x=\"44\" y=\"182\" font-size=\"13\">User "+(i+1)+" (Customer)</text>\n");
            s.append("<g transform=\"translate(0,"+(32*(i+1))+")\"></g>\n");
        }
        // right detail pane
        s.append("<rect x=\"400\" y=\"120\" width=\"780\" height=\"560\" rx=\"8\" fill=\""+CARD+"\" stroke=\"#e6eef5\"/>\n");
        s.append("<text x=\"420\" y=\"144\" font-size=\"16\" font-weight=\"700\">Selected user detail</text>\n");
        s.append("<text x=\"420\" y=\"174\" font-size=\"13\">Name: John Doe</text>\n");
        s.append("<text x=\"420\" y=\"198\" font-size=\"13\">Role: Customer</text>\n");
        s.append("<text x=\"420\" y=\"222\" font-size=\"13\">GovID: C-1001</text>\n");
        s.append("<text x=\"420\" y=\"246\" font-size=\"13\">Email: john@example.com</text>\n");
        s.append("<rect x=\"420\" y=\"280\" width=\"120\" height=\"34\" rx=\"6\" fill=\""+PRIMARY+"\"/>\n");
        s.append("<text x=\"452\" y=\"304\" font-size=\"13\" fill=\"#fff\">Edit</text>\n");
        s.append("<text x=\"520\" y=\"304\" font-size=\"13\" fill=\""+MUTED+"\"> [Delete]</text>\n");
        s.append("</svg>");
        return s.toString();
    }

    private static String createShipmentSvg() {
        int W=1200,H=800; StringBuilder s=new StringBuilder(); s.append(svgHeader(W,H));
        s.append("<rect width=\"100%\" height=\"100%\" fill=\""+BG+"\"/>\n");
        s.append("<text x=\"20\" y=\"40\" font-size=\"20\" font-weight=\"700\">Create Shipment — Wizard</text>\n");
        // steps
        s.append("<rect x=\"20\" y=\"80\" width=\"1160\" height=\"56\" rx=\"8\" fill=\"#fff\" stroke=\"#e6eef5\"/>\n");
        String[] steps={"1 Sender","2 Recipient","3 Package","4 Review"}; int sx=40;
        for(int i=0;i<steps.length;i++){
            s.append("<rect x=\""+sx+"\" y=\"92\" width=\"220\" height=\"32\" rx=\"6\" fill=\""+(i==0?PRIMARY:CARD)+"\"/>\n");
            s.append("<text x=\""+(sx+18)+"\" y=\"114\" font-size=\"13\" fill=\""+(i==0?"#fff":TEXT)+"\">"+steps[i]+"</text>\n");
            sx+=240;
        }
        // content sample
        s.append("<rect x=\"40\" y=\"160\" width=\"1120\" height=\"520\" rx=\"8\" fill=\""+CARD+"\" stroke=\"#e6eef5\"/>\n");
        s.append("<text x=\"60\" y=\"196\" font-size=\"14\" font-weight=\"700\">Sender</text>\n");
        s.append("<text x=\"60\" y=\"224\" font-size=\"13\">Name*: [__________]  Contact*: [________]  Address*: [______________]</text>\n");
        s.append("<text x=\"60\" y=\"260\" font-size=\"13\">[Back] [Next]</text>\n");
        s.append("</svg>");
        return s.toString();
    }

    private static String deliverySvg() {
        int W=1200,H=800; StringBuilder s=new StringBuilder(); s.append(svgHeader(W,H));
        s.append("<rect width=\"100%\" height=\"100%\" fill=\""+BG+"\"/>\n");
        s.append("<text x=\"20\" y=\"40\" font-size=\"20\" font-weight=\"700\">Driver — Deliveries</text>\n");
        s.append("<rect x=\"20\" y=\"80\" width=\"400\" height=\"640\" rx=\"8\" fill=\""+CARD+"\" stroke=\"#e6eef5\"/>\n");
        s.append("<text x=\"40\" y=\"112\" font-size=\"14\" font-weight=\"700\">Assigned Deliveries</text>\n");
        for(int i=0;i<8;i++){
            s.append("<rect x=\"32\" y=\"128\" width=\"368\" height=\"36\" fill=\"#fff\" stroke=\"#f1f5f8\"/>\n");
            s.append("<text x=\"48\" y=\"152\" font-size=\"13\">ORD-"+(100+i)+" — Alice — Due: Today</text>\n");
            s.append("<g transform=\"translate(0,"+(36*(i+1))+")\"></g>\n");
        }
        s.append("<rect x=\"440\" y=\"80\" width=\"720\" height=\"640\" rx=\"8\" fill=\""+CARD+"\" stroke=\"#e6eef5\"/>\n");
        s.append("<text x=\"460\" y=\"112\" font-size=\"16\" font-weight=\"700\">ORD-102</text>\n");
        s.append("<text x=\"460\" y=\"140\" font-size=\"13\">Recipient: Alice</text>\n");
        s.append("<text x=\"460\" y=\"164\" font-size=\"13\">Address: 123 Main St</text>\n");
        s.append("<rect x=\"460\" y=\"200\" width=\"120\" height=\"36\" rx=\"6\" fill=\"#fff\" stroke=\"#dfe6eb\"/>\n");
        s.append("<text x=\"492\" y=\"224\" font-size=\"13\">Picked up</text>\n");
        s.append("<rect x=\"592\" y=\"200\" width=\"120\" height=\"36\" rx=\"6\" fill=\"#fff\" stroke=\"#dfe6eb\"/>\n");
        s.append("<text x=\"624\" y=\"224\" font-size=\"13\">Delivered</text>\n");
        s.append("</svg>");
        return s.toString();
    }
}
