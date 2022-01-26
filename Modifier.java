
public enum Modifier {
		MAX_SPEED(1), LIFE_SPAN(1);

		private int scale;

		public int getScale() { return scale; }

		public int getRandomMod() {
			return (int) (Math.random()*(scale * 2 + 1) - scale);
		}
		private Modifier(int scaleIn) {
			scale = scaleIn;
		}
	}