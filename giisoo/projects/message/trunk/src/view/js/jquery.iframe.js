/**
 * 
 */

(function($) {
	var __history = [];
	var __afterload = false;
	$.fn.iframe = function(command) {
		var that = $(this);
		// var __history = [];//window.__history | [];
		// window.__history = __history;
		if (command == 'reload') {
			reload();
		} else if (command == 'back') {
			back();
		} else if(typeof(command) == 'string') {
			load(command);
		} else {
			if(typeof(command) == 'function') {
				__afterload = command;
			}
			hook();
		}

		function history(url) {
			if (url && url.length > 0) {
				if (__history[__history.length - 1] !== url) {
					__history.push(url);
				}
			}
		}

		function back() {
			if (__history.length > 1) {

				var h = __history.pop();
				h = __history.pop();

				load(h);
			}
		}

		function show(html) {

			try {
				that.html(html);
				__afterload && __afterload();
			} catch (e) {
				console.error(e);
			}

			hook();

		}

		function hook() {
			/**
			 * hook all the <a> tag
			 */
			that.find('a').each(function(i, e) {
				e = $(e);
				var href = e.attr('href');
				var target = e.attr('target');
				if (target == undefined && href != undefined
						&& (href.indexOf('javascript') == -1)
						&& (href.indexOf('#') != 0)) {

					e.click(function(e1) {
								var href = $(this).attr('href');
								if (href != undefined) {
									load(href);
								}

								// console.log(href);
								e1.preventDefault();
							});
				}
			});

			/**
			 * hook all <form> to smooth submit
			 */
			that.find('form').submit(function(e) {
				e.preventDefault();

				var form = e.target;

				var beforesubmit = $(form).attr('beforesubmit');
				if (typeof window[beforesubmit] === 'function') {
					if (!window[beforesubmit](form)) {
						return;
					}
				}

				/**
				 * check the bad flag
				 */
				var bad = $(form)
						.find("input[bad=1], textarea[bad=1], select[bad=1]");
				if (bad.length > 0) {
					bad[0].focus();
					return;
				}
				var bb = $(form)
						.find("input[required=true], select[required=true]");
				for (i = 0; i < bb.length; i++) {
					var e = $(bb[i]);
					if (e.val() == '') {
						e.focus();
						return;
					}
				}

				var url = form.action;

				if (form != undefined && url != undefined) {

					processing && processing.show();

					if (form.method == 'get') {
						var data = $(form).serialize();

						var __url = '';
						if (url.indexOf('?') > 0) {
							__url = url + '&' + data;
						} else {
							__url = url + '?' + data;
						}
						if (__history.length > 0
								&& __history[__history.length - 1] == __url) {
							__history.pop();
						}
						__history.push(__url);

						if (__url.indexOf('?') > 0) {
							__url += '&' + new Date().getTime();
						} else {
							__url += '?' + new Date().getTime();
						}

						$.get(__url, {}, function(d) {
									show(d);
									processing && processing.hide();
								});

					} else {
						var data = new FormData(form);

						var xhr = new XMLHttpRequest();
						xhr.open("POST", url);
						xhr.overrideMimeType("multipart/form-data");
						xhr.send(data);

						xhr.onreadystatechange = function() {
							if (xhr.readyState == 4) {
								if (xhr.status == 200) {
									show(xhr.responseText);
									processing && processing.hide();
								}
							}
						}

					}
				}

			});

		}

		function reload() {
			// console.log('reload:' + __history);
			if (__history.length > 0) {
				load(__history.pop());
				return true;
			}

			return false;
		}

		function load(uri) {
			processing && processing.show();

			if (__history.length > 0 && __history[__history.length - 1] == uri) {
				__history.pop();
			}
			__history.push(uri);

			// $('#page').attr('src', uri);
			if (uri.indexOf('?') > 0) {
				uri += '&' + new Date().getTime();
			} else {
				uri += '?' + new Date().getTime();
			}
			$.ajax({
						url : uri,
						type : 'GET',
						data : {},
						error : function(d) {
							processing && processing.hide();
							window.location.href = "/";
						},
						success : function(d, status, xhr) {
							processing && processing.hide();
							var resp = {
								"status" : xhr.getResponseHeader('status')
							};
							if (resp.status == '401') {
								window.location.href = "/";
							} else {
								show(d);
							}
						}
					})
		}
	}

})(jQuery);
