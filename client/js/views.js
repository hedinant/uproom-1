var
	AccountMenuView = Backbone.View.extend({
		events: {'click [data-id=signout]': function(event) {
			app.deactivate();
			$('body').trigger('click'); // Hide account menu
			return false;
		}},

		reset: function(user) { this.$('[data-id=name]').html(user.name); return this }
	}),

	// Get username/password and pass to backend for validation
	AuthView = Backbone.View.extend({
		events: {
			'click [data-id=submit]': function() {
				// Call backend auth method
				// WARN Change .get() to .post() when work with real backend
				$.get(AUTH_URL, {
					password: this.$('[data-id=password]').val(),
					username: this.$('[data-id=username]').val()
				}, (function(data) { this.callback[data.success ? 'success' : 'fail'](data) }).bind(this), 'json');

				return false;
			}
		},

		// Define callbacks to be called on auth try
		setCallback: function(funcs) {
			this.callback = this.callback || {};
			_.extend(this.callback = this.callback || {}, funcs);
			return this;
		}
	}),

	BaseView = Backbone.View.extend({
		initialize: function(options) {
			Backbone.View.prototype.initialize.call(this, options);
			this.layout = {};
		}
	}),

	// Manage application core views
	DevicesView = BaseView.extend({
		activate: function() {
			this.layout.auth.$el.hide();

			_.invoke([
				this.layout.accountMenu.$el,
				this.layout.list.$el,
				this.layout.sidebar.$el
			], 'show');
			
			return this;
		},

		deactivate: function() {
			this.layout.auth.$el.show();
			
			_.invoke([
				this.layout.accountMenu.$el,
				this.layout.list.$el,
				this.layout.sidebar.$el
			], 'hide');

			return this;
		},

		initialize: function(options) {
			BaseView.prototype.initialize.call(this, options);
			this.user = {};
		},

		render: function() {
			this.layout.accountMenu = new AccountMenuView({el: '#fat-menu'});
			this.layout.auth        = new AuthView({el: '#auth'});
			this.layout.list        = new ListView({el: '#list'});
			this.layout.sidebar     = new SidebarView({el: '#sidebar'});
			return this;
		}
	}),

	// List of devices itself
	ListView = BaseView.extend({
		add: function(model) {
			this.addItem(_.last(this.layout.items = this.layout.items.concat((new this.ItemView({
				model: model
			})).render())).el);
			
			return this;
		},

		// Append actual html element to the DOM
		addItem: function(el) { this.$('[data-id=list-container]').append(el); return this },

		// Clear internal items storage and DOM structure
		clear: function() { _.invoke(this.layout.items, 'remove'); this.layout.items = []; return this },

		// List item view
		ItemView: Backbone.View.extend({
			initialize: function(options) {
				Backbone.View.prototype.initialize.call(this, options);

				// Catch model change event
				this.model.on('change', this.render, this);
			},

			render: function() {
				this.$el.html(this.template({
					state: this.model.get('state').toLowerCase() == 'on' ? 'Вкл' : 'Выкл',
					title: this.model.get('name')
				}));

				this.$el.data('id', this.model.id);

				return this;
			},

			tagName: 'tr',
			template: Handlebars.compile('<td>{{ title }}</td><td>{{ state }}</td>')
		}),

		reset: function(collection) {
			this.collection = collection;
			
			// Create new item once collection gets new model
			this.collection.on('add', this.add, this);

			this.clear();
			this.collection.each(this.add, this);

			return this;
		}
	}),
	
	SidebarView = Backbone.View;