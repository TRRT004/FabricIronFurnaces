package ironfurnaces.api;

public interface IHeaterSource {
	double getEnergy();
	void extractEnergy(double amount);
}
