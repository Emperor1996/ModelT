if (typeof com === 'undefined' || com === null) {
	var com = {sap : {ISCE : {}}};
}
if (typeof com.sap === 'undefined' || com.sap === null) {
	com.sap = {ISCE : {}};
}
if (typeof com.sap.ISCE === 'undefined' || com.sap.ISCE === null) {
	com.sap.ISCE = {};
}

/**
 * In-Store Customer Engagement Paginator Handler. 
 * It is intended to overwrite the targets of the rendered Purchase History paginators with dedicated JavaScript functions, on the fly. 
 * We want to prevent roundtrips for performance and usability reasons.
 * This functionality can be deactivated by modifying the checkPrerequisites() first condition statement (e.g. provide another non existing ID).
 * The standard paginator UI is rendered and we rely on the following style classes and elements for the functionality to work properly: "ul.pagination", "li.active", ".pagination-bar .form-group'" and "tr.responsive-table-item"
 */
com.sap.ISCE.PaginatorHandler = {
		
		ISCE_UL_PAGINATION_STYLE_CLASS_NAME : ".isce-history .pagination",
		ISCE_LI_ACTIVE_STYLE_CLASS_NAME : ".active",
		ISCE_SORT_STYLE_CLASS_NAME : ".isce-history .pagination-bar .form-group",
		ISCE_TABLE_ITEM_STYLE_CLASS_NAME : ".isce-history tr.responsive-table-item",
		ISCE_360_DEFAULT_PAGINATION_LINK : "[href*='my-account/customer360']",
		ISCE_360_JS_PAGINATION_LINK : "[href*='displayPage']",
		SHIFT_RIGHT : 1,
		SHIFT_LEFT : -1,
		
		currentLinkClass : undefined,
		inActiveLinkClass : undefined,
		currentPage : undefined,
		firstPage: undefined,
		maxPageNumber : undefined,
		numberDisplayedPages : undefined,
		
		/**
		 * Procedure for changing the HREF content with dedicated JavaScript. Styling modification is also supported.  
		 */
		changePaginationLink : function(paginationEntry, paginationFunctionl, styleClasses, linkContent, linkPattern) {
			
			var linkSearchPattern = (linkPattern)? linkPattern : this.ISCE_360_DEFAULT_PAGINATION_LINK;
			var link = paginationEntry.find(linkSearchPattern);
			if (!link.length){
				return;
			} 
			link.attr('href', 'javascript:com.sap.ISCE.PaginatorHandler.' + paginationFunctionl + ';');
			if (styleClasses) {
				link.attr('class', styleClasses);
			}
			if (linkContent) {
				link.html(linkContent);
			}
		},
		
		/**
		 * Setter for the variable com.sap.ISCE.PaginatorHandler.currentPage, depending on the number of displayed pages inside the paginator and (if set) depending on the requested page number via URL parameter &page=x.
		 */
		setCurrentPage : function() {
			
			if (com.sap.ISCE.pageNumberDisplayed < 0) {
				// Page requested is out of range
				com.sap.ISCE.PaginatorHandler.currentPage = 0;
				return;
			}
			if (com.sap.ISCE.pageNumberDisplayed > com.sap.ISCE.maxNumberOfPages) {
				// Page requested is out of range
				com.sap.ISCE.PaginatorHandler.currentPage = com.sap.ISCE.maxNumberOfPages;
				return;
			}
			com.sap.ISCE.PaginatorHandler.currentPage = com.sap.ISCE.pageNumberDisplayed;
		},
		
		/**
		 * Retrieves the index of the active element inside the given HTML paginator. If not found, we retrieve the last valid number of the paginator list.
		 */
		getActive : function(pagingEntries) {
			
			if (com.sap.ISCE.PaginatorHandler.isParameterNotSet(pagingEntries)) {
				return -1;
			}
			var max = pagingEntries.length;
			var idx = pagingEntries.filter(com.sap.ISCE.PaginatorHandler.ISCE_LI_ACTIVE_STYLE_CLASS_NAME).index();
			if (idx === -1) {
				return max-2;
			}
			return idx;
		},
		
		/**
		 * Setter for the variable com.sap.ISCE.PaginatorHandler.firstPage, depending (if set) on the requested page number via URL parameter &page=x and depending on the number of displayed paginator numbers.
		 * Paginator sample: < 2 3 4 5 > will deliver 2. 
		 */
		setFirstPage : function(indexActive) {
			
			if (com.sap.ISCE.pageNumberDisplayed < 0) {
				// Page requested is out of range
				com.sap.ISCE.PaginatorHandler.firstPage = 0;
				return;
			}
			var intervalIndex = indexActive - 1;
			if ( intervalIndex < 0) {
				intervalIndex = 0;
			}
			com.sap.ISCE.PaginatorHandler.firstPage = com.sap.ISCE.PaginatorHandler.currentPage - intervalIndex;
		},		
		
		/**
		 * Adapts for each Paginators (Top + Bottom) the targets of the displayed entries with dedicated JavaScript functions and adapts the displayed results in the table accordingly.
		 */
		changeISCETransactionHistoryPaginationLinks : function() {

			$(com.sap.ISCE.PaginatorHandler.ISCE_SORT_STYLE_CLASS_NAME).hide();
			if (com.sap.ISCE.PaginatorHandler.isParameterNotSet(com.sap.ISCE.PaginatorHandler.currentPage)) {
				com.sap.ISCE.PaginatorHandler.setCurrentPage();	
			} 
			var paginators = $(com.sap.ISCE.PaginatorHandler.ISCE_UL_PAGINATION_STYLE_CLASS_NAME);
			
			paginators.each( function() {
				
				var pagingEntries = $(this).children();
				var noOfEntries = pagingEntries.length;	
				com.sap.ISCE.PaginatorHandler.numberDisplayedPages = noOfEntries - 2;
				var activePageIndex = com.sap.ISCE.PaginatorHandler.getActive(pagingEntries);
				com.sap.ISCE.PaginatorHandler.setFirstPage(activePageIndex);
				var inactivePageIndex;
				var previousNextCloneIndex;
				
				if (activePageIndex === noOfEntries - 2) {

					// Use cases: Zero paginator number to display: < > or only one paginator number to display: < 1 >
					if (com.sap.ISCE.PaginatorHandler.numberDisplayedPages === 0 || com.sap.ISCE.PaginatorHandler.numberDisplayedPages === 1) {
						inactivePageIndex = activePageIndex;
						if (com.sap.ISCE.PaginatorHandler.currentPage === com.sap.ISCE.PaginatorHandler.maxPageNumber) {
							// At the end of the list? take the Previous icon
							previousNextCloneIndex = 0;
						} else {
							// Takes the Next icon
							previousNextCloneIndex = noOfEntries -1;
						}
					} else {
						// Focus on the last LI list?
						inactivePageIndex = activePageIndex - 1;
						previousNextCloneIndex = 0;
					}
				}
				else {
					inactivePageIndex = activePageIndex + 1;
					previousNextCloneIndex = noOfEntries -1;
				}
				
				//Determine class for currently active page
				com.sap.ISCE.PaginatorHandler.currentLinkClass = $(pagingEntries[activePageIndex]).attr('class');
				com.sap.ISCE.PaginatorHandler.inActiveLinkClass = $(pagingEntries[inactivePageIndex]).attr('class');
				var newPreviousPageEntry;
				
				// Handle Previous and Next Page links
				if (previousNextCloneIndex === 0) {
					var nextPageClasses = $(pagingEntries[noOfEntries -1]).children().first().attr('class');
					
					//handle Next Page link
					newPreviousPageEntry = $(pagingEntries[previousNextCloneIndex]).clone();
					com.sap.ISCE.PaginatorHandler.changePaginationLink(newPreviousPageEntry, 'nextPage()', nextPageClasses);
					$(pagingEntries[noOfEntries -1]).empty().append(newPreviousPageEntry.children());
					
					//handle Previous Page link
					com.sap.ISCE.PaginatorHandler.changePaginationLink($(pagingEntries[previousNextCloneIndex]), 'previousPage()');
				}
				else  {
					//handle previous Page link
					var previousPageClasses = $(pagingEntries[0]).children().first().attr('class');
					newPreviousPageEntry = $(pagingEntries[previousNextCloneIndex]).clone();
					com.sap.ISCE.PaginatorHandler.changePaginationLink(newPreviousPageEntry, 'previousPage()', previousPageClasses);
					$(pagingEntries[0]).empty().append(newPreviousPageEntry.children());
					
					//handle next Page link
					com.sap.ISCE.PaginatorHandler.changePaginationLink($(pagingEntries[previousNextCloneIndex]), 'nextPage()');
				}

				
				var newDisplayPageEntry;
				var cloneTemplate = $(pagingEntries[inactivePageIndex]).clone();
				
				//handle direct page links
				for (var i = 1; i < noOfEntries - 1; i++) {
					newDisplayPageEntry = cloneTemplate.clone();
					com.sap.ISCE.PaginatorHandler.changePaginationLink(newDisplayPageEntry, 'displayPage(' + (com.sap.ISCE.PaginatorHandler.firstPage + i - 1) +')', undefined, com.sap.ISCE.PaginatorHandler.firstPage + i);
					$(pagingEntries[i]).empty().append(newDisplayPageEntry.children());
				}
			});
			
			this.setDataAfterPaging();
		},
		
		/**
		 * Functionality executed when the next page element ">" is clicked. It takes care to actualize the displayed paginator content and table content.  
		 */
		nextPage : function() {
			
			if (this.currentPage < this.maxPageNumber) {
				this.currentPage++;
				if (this.numberDisplayedPages > 0 && this.currentPage > this.firstPage + this.numberDisplayedPages - 1) {
					this.shiftPageLinks(this.SHIFT_RIGHT);
				}
				if (this.numberDisplayedPages < 2 || this.currentPage <= this.firstPage + this.numberDisplayedPages - 1)  {
					this.setClassesAfterPaging();
				}
				this.setDataAfterPaging();
			}
		},

		/**
		 * Functionality executed when the previous page element "<" is clicked. It takes care to actualize the displayed paginator content and table content.  
		 */
		previousPage : function() {
			
			if (this.currentPage > 0) {
				this.currentPage--;
				if (this.numberDisplayedPages > 0 &&  this.currentPage < this.firstPage) {
					this.shiftPageLinks(this.SHIFT_LEFT);
				}
				if (this.numberDisplayedPages < 2 || this.currentPage >= this.firstPage) {
					this.setClassesAfterPaging();
				}
				this.setDataAfterPaging();
			}
		},

		/**
		 * Functionality executed when an explicit paginator number is clicked. It takes care to actualize the displayed paginator content and table content.
		 */
		displayPage : function(pageNo) {
			
			if (this.currentPage !== pageNo) {
				this.currentPage = pageNo;
				this.setClassesAfterPaging();
				this.setDataAfterPaging();
			}
		},
		
		/**
		 * Shifts the page link content for every paginators (Top and Bottom)
		 */
		shiftPageLinks : function(shift) {
			
			this.firstPage = this.firstPage + shift;
			var paginators = $(com.sap.ISCE.PaginatorHandler.ISCE_UL_PAGINATION_STYLE_CLASS_NAME);
			
			paginators.each( function() {
				var pagHandler = com.sap.ISCE.PaginatorHandler;
				var pagingEntries = $(this).children();
				//only one Page link
				if (pagHandler.numberDisplayedPages === 1) {
					var html = $(pagingEntries[1]).html();
					if (shift === pagHandler.SHIFT_RIGHT) {
						html = html.replace(">" + pagHandler.currentPage + "<", ">" + (pagHandler.currentPage + 1) + "<")
					}
					else {
						html = html.replace(">" + (pagHandler.currentPage + 2) + "<", ">" + (pagHandler.currentPage + 1) + "<")
					}
					$(pagingEntries[1]).html(html)
					return;
				}
				//more than one Page link
				for (var i = 1; i <= pagHandler.numberDisplayedPages; i++) {
					pagHandler.changePaginationLink($(pagingEntries[i]), 'displayPage(' + (pagHandler.firstPage + i - 1) +')', undefined, pagHandler.firstPage + i, pagHandler.ISCE_360_JS_PAGINATION_LINK);
				}
			})
		},
		
		/**
		 * Takes care to render the adequate styling class to every paginator elements.
		 */
		setClassesAfterPaging : function() {
			
			var paginators = $(com.sap.ISCE.PaginatorHandler.ISCE_UL_PAGINATION_STYLE_CLASS_NAME);
			
			paginators.each( function() {
				var pagingEntries = $(this).children();
				var pagHandler = com.sap.ISCE.PaginatorHandler;
				
				// previous page
				$(pagingEntries[0]).removeClass('disabled');
				if (pagHandler.currentPage === 0) {
					$(pagingEntries[0]).addClass('disabled');
				}
				
				//direct page links
				for (var i = 1; i <= pagHandler.numberDisplayedPages; i++) {
					var styleClass = (pagHandler.currentPage === pagHandler.firstPage + i - 1) ? pagHandler.currentLinkClass : pagHandler.inActiveLinkClass;
					$(pagingEntries[i]).attr('class', (styleClass) ? styleClass : "");
				}
				
				//next Page
				$(pagingEntries[pagHandler.numberDisplayedPages + 1]).removeClass('disabled');
				if (pagHandler.currentPage === pagHandler.maxPageNumber) {
					$(pagingEntries[pagHandler.numberDisplayedPages + 1]).addClass('disabled');
				}
			});
			
		},
		
		/**
		 * Takes care to adapt the content of the table depending on the requested page number. The data are stored in a predefined Array com.sap.ISCE.purchaseHistoryDataArray.
		 */
		setDataAfterPaging : function() {
			
			if (com.sap.ISCE.PaginatorHandler.isParameterNotSet(com.sap.ISCE.PaginatorHandler.currentPage)) {
				com.sap.ISCE.PaginatorHandler.setCurrentPage();	
			} 

			var trTableItems = $(com.sap.ISCE.PaginatorHandler.ISCE_TABLE_ITEM_STYLE_CLASS_NAME);
			
			trTableItems.each( function( index ) {
				
				var newIndexPage = index + (com.sap.ISCE.PaginatorHandler.currentPage * com.sap.ISCE.numberOfResultsPerPage);
				var historyData = com.sap.ISCE.purchaseHistoryDataArray[newIndexPage]
				if (newIndexPage < com.sap.ISCE.purchaseHistoryDataArray.length) {
					$(this).show();
					var td;
					td = $(this).find('#purchaseHistory_Type_' + (index+1));
					if (td.length) {
						if (historyData[1] === null || historyData[1].trim()==='') {
							td.html(historyData[0]);
						} else {
							td.html(historyData[0]+' (<a href="'+com.sap.ISCE.fullUrlToOrder+historyData[1]+'" class="responsive-table-link">'+historyData[1]+'</a>)');
						}
					}
					td = $(this).find('#purchaseHistory_grossSalesVolumePrice_' + (index+1));
					if (td.length) {
						td.html(historyData[2]);
					}
					td = $(this).find('#purchaseHistory_Date_' + (index+1))
					if (td.length) {
						td.html(historyData[3]);
					}
				} else {
					$(this).hide();
				}

			});
		},
		
		/**
		 * Checks the mandatory prerequisites of the paginator handler.<br />
		 * A special DIV container (with ID #customer360PurchaseHistory) must be rendered and some special variables must also be set.
		 *  
		 *  @returns {boolean} Returns true if the prerequisites are met, false otherwise.
		 */
		checkPrerequisites : function() {
			
			if ($('#customer360PurchaseHistory').length) {
				var pagePrerequisites = com.sap.ISCE.PaginatorHandler.isParameterNotSet(com.sap.ISCE.numberOfResultsPerPage) || 
				                        com.sap.ISCE.PaginatorHandler.isParameterNotSet(com.sap.ISCE.pageNumberDisplayed ||
				                        com.sap.ISCE.PaginatorHandler.isParameterNotSet(com.sap.ISCE.maxNumberOfPages)); 
				if (pagePrerequisites ||
					com.sap.ISCE.PaginatorHandler.isParameterNotSet(com.sap.ISCE.fullUrlToOrder) ||
					com.sap.ISCE.PaginatorHandler.isParameterNotSet(com.sap.ISCE.purchaseHistoryDataArray)) {
				    return false;
				}
				com.sap.ISCE.PaginatorHandler.paginatorMaxRange = com.sap.ISCE.maxNumberOfPages -2; 
				com.sap.ISCE.PaginatorHandler.maxPageNumber = com.sap.ISCE.maxNumberOfPages;
				return true;
			}
			return false;
		},
		
		/**
		 * Checks if a given parameter is NOT set.
		 *  
		 *  @returns {boolean} Returns false if the parameter is set, true otherwise.
		 */
		isParameterNotSet : function(parameter) {
			
			if (typeof parameter === 'undefined' || parameter === null) {
				return true;
			}
			return false;
		}
};