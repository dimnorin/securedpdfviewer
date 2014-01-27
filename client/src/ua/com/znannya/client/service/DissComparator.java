package ua.com.znannya.client.service;

import java.util.Comparator;

import org.jivesoftware.smack.znannya.dao.Dissertation;
import org.jivesoftware.smack.znannya.dao.Entry;
import org.jivesoftware.smack.znannya.dao.Publication;

import ua.com.znannya.client.ui.DocumentsPane;

/** Comparator for Dissertations sorting */
public class DissComparator implements Comparator<Entry> {
	private String field;
	private boolean ascending;
	
	public DissComparator(String f, boolean asc){ 
		field = f; ascending = asc; 
	}

	public int compare(Entry e1, Entry e2){
		if ( e1 instanceof Publication && e2 instanceof Dissertation ){
			Dissertation d = (Dissertation) e2;
			Publication p = (Publication) e1;
			for (int i = 0; i < DocumentsPane.COL_NAMES.length; i++) {
				if ( DocumentsPane.COL_NAMES[i].equals(field) )
					switch (i) {
						case 0: return compareFields( p.getName(), d.getName() );
						case 1: return compareFields( p.getAuthor(), d.getAuthor() );
				        case 2: return compareFields( p.getIsbn(), "" );
				        case 3: return compareFields( p.getPublisherName(), "" );
				        case 4: return compareFields( p.getUDK_idx(), "" );
				        case 5: return compareFields( p.getBBK_idx(), "" );
						case 6: return compareFields( "", d.getKind() );
						case 7: return compareFields( "", d.getCode() );
						case 8: return compareFields( "", d.getOrganization() );
						case 9: return compareFields( p.getCity(), d.getCity() );
						case 10: return compareFields( Integer.parseInt(p.getYear()), Integer.parseInt(d.getYear()) );
						case 11: return compareFields( 0, d.getPages() );
						case 12: return compareFields( "", d.getDACNTI_code() );
						case 13: return compareFields( "", d.getUDK_idx() );
						case 14:return compareFields( p.getDescription(), d.getDescription() );
					}
			}
		}
		if ( e1 instanceof Dissertation && e2 instanceof Publication ){
			Dissertation d = (Dissertation) e1;
			Publication p = (Publication) e2;
			for (int i = 0; i < DocumentsPane.COL_NAMES.length; i++) {
				if ( DocumentsPane.COL_NAMES[i].equals(field) )
					switch (i) {
						case 0: return compareFields( d.getName(), p.getName() );
						case 1: return compareFields( d.getAuthor(), p.getAuthor() );
				        case 2: return compareFields( "", p.getIsbn() );
				        case 3: return compareFields( "", p.getPublisherName() );
				        case 4: return compareFields( "", p.getUDK_idx() );
				        case 5: return compareFields( "", p.getBBK_idx() );
						case 6: return compareFields( d.getKind(), "" );
						case 7: return compareFields( d.getCode(), "" );
						case 8: return compareFields( d.getOrganization(), "" );
						case 9: return compareFields( d.getCity(), p.getCity() );
						case 10: return compareFields( Integer.parseInt(d.getYear()), Integer.parseInt(p.getYear()) );
						case 11: return compareFields( d.getPages(), 0 );
						case 12: return compareFields( d.getDACNTI_code(), "" );
						case 13: return compareFields( d.getUDK_idx(), "" );
						case 14:return compareFields( d.getDescription(), p.getDescription() );
					}
			}
		}
		
		if ( e1 instanceof Dissertation ){
			Dissertation d1 = (Dissertation) e1;
			Dissertation d2 = (Dissertation) e2;
			for (int i = 0; i < DissertationService.COL_NAMES.length; i++) {
				if ( DissertationService.COL_NAMES[i].equals(field) )
					switch (i) {
						case 0: return compareFields( d1.getName(), d2.getName() );
						case 1: return compareFields( d1.getAuthor(), d2.getAuthor() );
						case 2: return compareFields( d1.getKind(), d2.getKind() );
						case 3: return compareFields( d1.getCode(), d2.getCode() );
						case 4: return compareFields( d1.getOrganization(), d2.getOrganization() );
						case 5: return compareFields( d1.getCity(), d2.getCity() );
						case 6: return compareFields( Integer.parseInt(d1.getYear()), Integer.parseInt(d2.getYear()) );
						case 7: return compareFields( d1.getPages(), d2.getPages() );
						case 8: return compareFields( d1.getDACNTI_code(), d2.getDACNTI_code() );
						case 9: return compareFields( d1.getUDK_idx(), d2.getUDK_idx() );
						case 10:return compareFields( d1.getDescription(), d2.getDescription() );
					  }
			  }
		}
		if ( e1 instanceof Publication ){
			Publication p1 = (Publication) e1;
			Publication p2 = (Publication) e2;
			for (int i = 0; i < PublicationService.COL_NAMES.length; i++) {
				if ( PublicationService.COL_NAMES[i].equals(field) )
					switch (i) {
		    			case 0: return compareFields( p1.getName(), p2.getName() );
		    			case 1: return compareFields( p1.getAuthor(), p2.getAuthor() );
				        case 2: return compareFields( p1.getIsbn(), p2.getIsbn() );
				        case 3: return compareFields( p1.getPublisherName(), p2.getPublisherName() );
				        case 4: return compareFields( p1.getUDK_idx(), p2.getUDK_idx() );
				        case 5: return compareFields( p1.getBBK_idx(), p2.getBBK_idx() );
				        case 6: return compareFields( p1.getCity(), p2.getCity() );
				        case 7: return compareFields( Integer.parseInt(p1.getYear()), Integer.parseInt(p2.getYear()) );
				        case 8: return compareFields( p1.getDescription(), p2.getDescription() );
		    		}				
			}
		}
	    return 0;
	}

	private int compareFields(Comparable v1, Comparable v2){
		return ascending ? v1.compareTo(v2) : v2.compareTo(v1);
	}
}
