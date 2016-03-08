var _itemId;


(function() {
	$('.carousel').on('click', imageClicked);

	$('input[name="watcha_size"]').on('change', function(event) {
		$('.watcha').css('width', event.currentTarget.value);
	});

	$('select[id="image_category"]').on('change', function(event) {
		console.log(event.currentTarget.value);
		
		queryImage(['hd', 'ud', 'cos'], _itemId, event.currentTarget.value);
	});
	
	var carousel = $('#carousel');

	carousel.owlCarousel({
		items: 5
	});

	$('.carousel_direction--prev').on('click', function() {
		carousel.trigger('owl.prev');
	});

	$('.carousel_direction--next').on('click', function() {
		carousel.trigger('owl.next');
	});
})();

function queryItems() {
	$.ajax('').done(function(data) {
		$('.carousels .carousel').remove();

		if (!data || !data['list'] || data.list.length == 0) {
			console.log('empty data!');

			return;
		}

		var boxEl = $('carousels');

		data.forEach(function(item) {
			var itemEl = $('<a href="" class="carousel"><img src="' + item['url'] + '" class="carousel__image"></a>');

			boxEl.append(itemEl);
		});
	});
}

function imageClicked() {
	var itemId = $(event.currentTarget).data('item');
	var a = itemId.split(',');
	_itemId = a[0];
	
	$('select[id="image_category"]').val(a[1]);
	
	queryImage(['hd', 'ud', 'cos'], a[0], a[1]);
}

function queryImage(type, itemId, categoryId) {
	var currentType = type.pop();
	
	$.ajax('query' + currentType + '?itemkey='+ itemId+'&category='+ categoryId +'&count=20').done(function(data) {
		$('.watcha_box--' + currentType + ' .watcha').remove();

		if (!data || !data['list'] || data.list.length == 0) {
			console.log('empty data!');

			return;
		}

		var boxEl = $('.watcha_box--' + currentType);

		data.list.forEach(function(item) {
			var el = $('<div class="watcha">' +
				'<img src="' + item['url'] + '" class="watcha__image">' +
				'<span class="watcha__value">' + item['value'] + '</span>' +
				'</div>');

			boxEl.append(el);

			el.css('width', $('input[name="watcha_size"]:checked').attr('value'));
		});

		if (type.length === 0) return;

		queryImage(type, itemId, categoryId);
	});
}