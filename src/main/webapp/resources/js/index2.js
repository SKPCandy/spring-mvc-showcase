(function() {
	$('.carousel').on('click', imageClicked);

	$('input[name="watcha_size"]').on('change', function(event) {
		$('.watcha').css('width', event.currentTarget.value);
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

function imageClicked(event) {
	var itemId = $(event.currentTarget).data('item');
	
	queryImage(['hd', 'ud', 'cos'], itemId);
}

function queryImage(type, itemId) {
	var currentType = type.pop();
	
	var a = itemId.split(',');

	if (currentType === undefined) return;
	$.ajax('query' + currentType + '?itemkey='+ a[0]+'&category='+ a[1] +'&count=20').done(function(data) {
		$('.watcha_box--' + currentType + ' .watcha').remove();

		if (!data || !data['list'] || data.list.length == 0) {
			console.log('empty data!');

			return;
		}

		var boxEl = $('.watcha_box--' + currentType);

		data.list.forEach(function(item) {
			boxEl.append('<div class="watcha">' +
				'<img src="' + item['url'] + '" class="watcha__image">' +
				'<span class="watcha__value">' + item['value'] + '</span>' +
				'</div>');
		});

		if (type.length === 0) return;

		queryImage(type, itemId);
	});
}